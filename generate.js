// generate.js — 개발자 포트폴리오 웹사이트 생성 스크립트
// 실행: node generate.js
// 입력: data/resume/ (마크다운 이력서), data/photos/ (이미지)
// 출력: output/index.html, output/photos/

const fs = require('fs');
const path = require('path');

// ─── 경로 설정 ───

const ROOT = __dirname;
const DATA_PHOTOS = path.join(ROOT, 'data', 'photos');
const DATA_RESUME = path.join(ROOT, 'data', 'resume');
const OUTPUT_DIR = path.join(ROOT, 'output');
const OUTPUT_PHOTOS = path.join(OUTPUT_DIR, 'photos');

// ─── T001: output/ 디렉토리 준비 ───

function prepareOutputDir() {
  if (fs.existsSync(OUTPUT_DIR)) {
    fs.rmSync(OUTPUT_DIR, { recursive: true });
  }
  fs.mkdirSync(OUTPUT_DIR, { recursive: true });
  fs.mkdirSync(OUTPUT_PHOTOS, { recursive: true });
}

// ─── T002: 이미지 복사 및 PhotoCollection 생성 ───

const IMAGE_EXTENSIONS = ['.jpg', '.jpeg', '.png', '.gif', '.webp'];

function collectPhotos() {
  if (!fs.existsSync(DATA_PHOTOS)) {
    return { profilePhoto: null, galleryPhotos: [] };
  }

  const files = fs.readdirSync(DATA_PHOTOS)
    .filter(file => {
      const ext = path.extname(file).toLowerCase();
      return IMAGE_EXTENSIONS.includes(ext);
    })
    .sort();

  // 각 이미지를 output/photos/에 복사
  for (const file of files) {
    fs.copyFileSync(
      path.join(DATA_PHOTOS, file),
      path.join(OUTPUT_PHOTOS, file)
    );
  }

  return {
    profilePhoto: files.length > 0 ? files[0] : null,
    galleryPhotos: files
  };
}

// ─── T004: 테이블 행 셀 추출 ───

function parseTableRow(line) {
  if (!line.trim().startsWith('|')) return [];
  const cells = line.split('|');
  // 첫 번째와 마지막은 빈 문자열 (| 앞뒤)
  return cells.slice(1, -1).map(cell => cell.trim());
}

// ─── T005: 마크다운 서식 정리 ───

function cleanMarkdown(text) {
  if (!text) return '';
  return text
    .replace(/\*\*\*(.*?)\*\*\*/g, '$1')   // ***bold italic***
    .replace(/\*\*(.*?)\*\*/g, '$1')         // **bold**
    .replace(/\*(.*?)\*/g, '$1')             // *italic*
    .replace(/\\-/g, '-')                     // escaped dash
    .replace(/\\\+/g, '+')                   // escaped plus
    .replace(/\s+/g, ' ')                    // 연속 공백 정리
    .trim();
}

// 마크다운에서 볼드 텍스트만 추출
function extractBold(text) {
  const matches = text.match(/\*\*(.*?)\*\*/g);
  if (!matches) return [];
  return matches.map(m => m.replace(/\*\*/g, '').trim());
}

// 마크다운에서 이탤릭 텍스트만 추출
function extractItalic(text) {
  // 볼드(***나 **)가 아닌 순수 이탤릭만 추출
  const withoutBold = text.replace(/\*\*\*(.*?)\*\*\*/g, '').replace(/\*\*(.*?)\*\*/g, '');
  const matches = withoutBold.match(/\*(.*?)\*/g);
  if (!matches) return [];
  return matches.map(m => m.replace(/\*/g, '').replace(/;/g, '').trim()).filter(Boolean);
}

// ─── T003, T006, T007: 이력서 마크다운 파서 ───

function parseResume(mdContent) {
  const result = {
    name: '',
    phone: '',
    email: '',
    education: [],
    work: [],
    activities: [],
    skills: []
  };

  if (!mdContent || !mdContent.trim()) {
    result.name = 'Portfolio';
    return result;
  }

  const lines = mdContent.split('\n').filter(line => line.trim().length > 0);
  let currentSection = 'header';
  let lineIndex = 0;

  for (lineIndex = 0; lineIndex < lines.length; lineIndex++) {
    const line = lines[lineIndex];
    const cells = parseTableRow(line);
    if (cells.length === 0) continue;

    // 구분선 무시 (| ----- | :---- | 형태)
    if (cells[0] && /^[\s\-:]+$/.test(cells[0])) continue;

    const firstCell = cells[0] || '';

    // 섹션 전환 감지
    if (firstCell.includes('**EDUCATION**')) {
      currentSection = 'education';
      continue;
    }
    if (firstCell.includes('**WORK EXPERIENCE**')) {
      currentSection = 'work';
      continue;
    }
    if (firstCell.includes('**EXTRA-CURRICULAR ACTIVITIES**') || firstCell.includes('**EXTRA\\-CURRICULAR')) {
      currentSection = 'activities';
      continue;
    }
    if (firstCell.includes('**SKILLS AND INFORMATION**')) {
      currentSection = 'skills';
      continue;
    }

    // 섹션별 파싱
    switch (currentSection) {
      case 'header':
        parseHeader(cells, result, lineIndex, lines);
        break;
      case 'education':
        parseEducation(cells, result, lines, lineIndex);
        break;
      case 'work':
        parseWork(cells, result, lines, lineIndex);
        break;
      case 'activities':
        parseActivities(cells, result, lines, lineIndex);
        break;
      case 'skills':
        parseSkills(cells, result);
        break;
    }
  }

  if (!result.name) result.name = 'Portfolio';
  return result;
}

let headerLineCount = 0;

function parseHeader(cells, result, lineIndex, lines) {
  headerLineCount++;
  if (headerLineCount === 1) {
    // 첫 행: 이름
    result.name = cleanMarkdown(cells[0]);
  } else if (headerLineCount === 2) {
    // 둘째 행: 연락처
    const fullText = cells.join(' ');
    // 전화번호
    const phoneMatch = fullText.match(/\\?\+?\d[\d\s]+\d/);
    if (phoneMatch) {
      result.phone = phoneMatch[0].replace(/\\/g, '').trim();
    }
    // 이메일
    const emailMatch = fullText.match(/[\w.+-]+@[\w.-]+\.\w+/);
    if (emailMatch) {
      result.email = emailMatch[0];
    }
  }
}

function parseEducation(cells, result, lines, lineIndex) {
  const firstCell = cells[0] || '';
  const thirdCell = cells[2] || '';

  // 학위 행 감지: 볼드 텍스트 + 학교명 포함
  const boldParts = extractBold(firstCell);
  if (boldParts.length > 0 && (firstCell.includes('University') || firstCell.includes('College') || firstCell.includes('School'))) {
    const fullText = cleanMarkdown(firstCell);
    // "학위 - 학교, 위치" 패턴 분리
    const dashIndex = fullText.indexOf('-');
    let degree = '';
    let school = '';
    let location = '';

    if (dashIndex !== -1) {
      degree = fullText.substring(0, dashIndex).trim();
      const rest = fullText.substring(dashIndex + 1).trim();
      const commaIndex = rest.indexOf(',');
      if (commaIndex !== -1) {
        school = rest.substring(0, commaIndex).trim();
        location = rest.substring(commaIndex + 1).trim();
      } else {
        school = rest;
      }
    } else {
      degree = fullText;
    }

    const period = cleanMarkdown(thirdCell);

    result.education.push({ degree, school, location, period, gpa: '' });
  } else if (firstCell.toLowerCase().includes('gpa') && result.education.length > 0) {
    // GPA 행
    result.education[result.education.length - 1].gpa = cleanMarkdown(firstCell);
  }
}

function parseWork(cells, result, lines, lineIndex) {
  const firstCell = cells[0] || '';
  const thirdCell = cells[2] || '';

  // 회사명 행 감지: 볼드 텍스트 + 이탤릭(직책)
  const boldParts = extractBold(firstCell);
  const italicParts = extractItalic(firstCell);

  if (boldParts.length > 0 && italicParts.length > 0) {
    const company = boldParts[0];
    const role = italicParts[0];

    // 3번째 셀에서 장소와 기간 추출
    const locationBold = extractBold(thirdCell);
    const periodItalic = extractItalic(thirdCell);
    // 4번째 셀도 확인 (일부 항목은 4열에 장소/기간이 있음)
    const fourthCell = cells[3] || '';
    const locationBold4 = extractBold(fourthCell);
    const periodItalic4 = extractItalic(fourthCell);

    const location = locationBold.length > 0 ? locationBold[0] : (locationBold4.length > 0 ? locationBold4[0] : '');
    const period = periodItalic.length > 0 ? periodItalic[0] : (periodItalic4.length > 0 ? periodItalic4[0] : '');

    result.work.push({ company, role, location, period, description: '' });
  } else if (firstCell.startsWith('•') || (firstCell.length > 50 && result.work.length > 0)) {
    // 설명 행
    if (result.work.length > 0) {
      const desc = cleanMarkdown(firstCell);
      if (result.work[result.work.length - 1].description) {
        result.work[result.work.length - 1].description += ' ' + desc;
      } else {
        result.work[result.work.length - 1].description = desc;
      }
    }
  }
}

function parseActivities(cells, result, lines, lineIndex) {
  const firstCell = cells[0] || '';
  const thirdCell = cells[2] || '';
  const fourthCell = cells[3] || '';

  const boldParts = extractBold(firstCell);
  const italicParts = extractItalic(firstCell);

  if (boldParts.length > 0 && italicParts.length > 0) {
    const title = boldParts[0];
    const role = italicParts[0];

    const locationBold = extractBold(fourthCell.length > 0 ? fourthCell : thirdCell);
    const periodItalic = extractItalic(fourthCell.length > 0 ? fourthCell : thirdCell);

    const location = locationBold.length > 0 ? locationBold[0] : '';
    const period = periodItalic.length > 0 ? periodItalic[0] : '';

    result.activities.push({ title, role, location, period, description: '' });
  } else if (firstCell.length > 50 && result.activities.length > 0) {
    // 설명 행
    const desc = cleanMarkdown(firstCell);
    if (result.activities[result.activities.length - 1].description) {
      result.activities[result.activities.length - 1].description += ' ' + desc;
    } else {
      result.activities[result.activities.length - 1].description = desc;
    }
  }
}

function parseSkills(cells, result) {
  const firstCell = cells[0] || '';
  const secondCell = cells[1] || '';

  const boldParts = extractBold(firstCell);
  if (boldParts.length > 0 && secondCell.trim()) {
    result.skills.push({
      category: boldParts[0],
      value: cleanMarkdown(secondCell)
    });
  }
}

// ─── 이력서 파일 읽기 ───

function readResume() {
  if (!fs.existsSync(DATA_RESUME)) {
    console.warn('경고: data/resume/ 폴더가 없습니다.');
    return { name: 'Portfolio', phone: '', email: '', education: [], work: [], activities: [], skills: [] };
  }

  const mdFiles = fs.readdirSync(DATA_RESUME).filter(f => f.endsWith('.md'));
  if (mdFiles.length === 0) {
    console.warn('경고: data/resume/에 .md 파일이 없습니다.');
    return { name: 'Portfolio', phone: '', email: '', education: [], work: [], activities: [], skills: [] };
  }

  const mdPath = path.join(DATA_RESUME, mdFiles[0]);
  const mdContent = fs.readFileSync(mdPath, 'utf-8');
  headerLineCount = 0; // 파서 상태 초기화
  return parseResume(mdContent);
}

// ─── T008-T020: HTML 생성 ───

function generateHTML(resume, photos) {
  const heroSection = generateHero(resume, photos);
  const workSection = generateWorkSection(resume.work);
  const educationSection = generateEducationSection(resume.education);
  const activitiesSection = generateActivitiesSection(resume.activities);
  const skillsSection = generateSkillsSection(resume.skills);
  const gallerySection = generateGallerySection(photos);

  return `<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${escapeHTML(resume.name)} — Portfolio</title>
  <style>
    /* ─── 리셋 및 전역 ─── */
    *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
    html { scroll-behavior: smooth; }

    body {
      font-family: system-ui, -apple-system, 'Segoe UI', Roboto, sans-serif;
      line-height: 1.6;
      color: #222;
      background: #f9f9f9;
    }

    .container {
      max-width: 900px;
      margin: 0 auto;
      padding: 0 24px;
    }

    /* ─── 타이포그래피 ─── */
    h1 { font-size: 2.5rem; font-weight: 700; color: #111; }
    h2 { font-size: 1.75rem; font-weight: 600; color: #2563eb; margin-bottom: 24px; }
    h3 { font-size: 1.25rem; font-weight: 600; color: #333; }
    p { font-size: 1rem; color: #444; }

    section { padding: 48px 0; }

    /* ─── 히어로 ─── */
    .hero {
      text-align: center;
      padding: 64px 0 48px;
    }
    .hero-photo {
      width: 160px;
      height: 160px;
      border-radius: 50%;
      object-fit: cover;
      margin-bottom: 24px;
      box-shadow: 0 4px 20px rgba(0,0,0,0.1);
    }
    .hero h1 { margin-bottom: 8px; }
    .hero-subtitle {
      font-size: 1.2rem;
      color: #555;
      font-weight: 400;
    }
    .hero-contact {
      margin-top: 16px;
      font-size: 0.95rem;
      color: #666;
    }
    .hero-contact a {
      color: #2563eb;
      text-decoration: none;
    }
    .hero-contact a:hover { text-decoration: underline; }

    /* ─── 섹션 구분 ─── */
    .section-divider {
      border: none;
      border-top: 1px solid #e5e7eb;
      margin: 0;
    }

    /* ─── 경력/활동 카드 ─── */
    .experience-item {
      margin-bottom: 32px;
      padding: 24px;
      background: #fff;
      border-radius: 12px;
      box-shadow: 0 1px 4px rgba(0,0,0,0.06);
    }
    .experience-header {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      flex-wrap: wrap;
      gap: 8px;
      margin-bottom: 8px;
    }
    .experience-company { font-size: 1.25rem; font-weight: 600; color: #111; }
    .experience-role { font-size: 1rem; color: #2563eb; margin-bottom: 4px; }
    .experience-meta {
      font-size: 0.9rem;
      color: #888;
      text-align: right;
      white-space: nowrap;
    }
    .experience-description {
      font-size: 0.95rem;
      color: #444;
      line-height: 1.7;
      margin-top: 12px;
    }

    /* ─── 학력 카드 ─── */
    .education-item {
      margin-bottom: 20px;
      padding: 20px 24px;
      background: #fff;
      border-radius: 12px;
      box-shadow: 0 1px 4px rgba(0,0,0,0.06);
    }
    .education-header {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      flex-wrap: wrap;
      gap: 8px;
    }
    .education-degree { font-size: 1.1rem; font-weight: 600; color: #111; }
    .education-school { font-size: 0.95rem; color: #555; }
    .education-period { font-size: 0.9rem; color: #888; white-space: nowrap; }
    .education-gpa { font-size: 0.9rem; color: #666; margin-top: 6px; }

    /* ─── 기술 ─── */
    .skills-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
      gap: 16px;
    }
    .skill-item {
      padding: 16px 20px;
      background: #fff;
      border-radius: 12px;
      box-shadow: 0 1px 4px rgba(0,0,0,0.06);
    }
    .skill-category {
      font-size: 0.85rem;
      font-weight: 600;
      color: #2563eb;
      text-transform: uppercase;
      letter-spacing: 0.5px;
      margin-bottom: 4px;
    }
    .skill-value { font-size: 0.95rem; color: #444; }

    /* ─── 갤러리 ─── */
    .gallery-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
      gap: 16px;
    }
    .gallery-card {
      overflow: hidden;
      border-radius: 12px;
      box-shadow: 0 2px 8px rgba(0,0,0,0.08);
      transition: transform 0.3s ease, box-shadow 0.3s ease;
    }
    .gallery-card:hover {
      transform: scale(1.03);
      box-shadow: 0 8px 24px rgba(0,0,0,0.15);
    }
    .gallery-card img {
      width: 100%;
      height: 220px;
      object-fit: cover;
      display: block;
    }

    /* ─── 푸터 ─── */
    .footer {
      text-align: center;
      padding: 32px 0;
      font-size: 0.85rem;
      color: #aaa;
    }

    /* ─── 반응형 ─── */
    @media (max-width: 600px) {
      .container { padding: 0 16px; }
      h1 { font-size: 2rem; }
      h2 { font-size: 1.5rem; }
      section { padding: 32px 0; }
      .hero { padding: 40px 0 32px; }
      .hero-photo { width: 120px; height: 120px; }
      .experience-header { flex-direction: column; }
      .experience-meta { text-align: left; }
      .education-header { flex-direction: column; }
      .gallery-grid { grid-template-columns: 1fr; }
      .skills-grid { grid-template-columns: 1fr; }
    }
  </style>
</head>
<body>
  <div class="container">
${heroSection}
${workSection}
${educationSection}
${activitiesSection}
${skillsSection}
${gallerySection}
    <div class="footer">
      &copy; ${new Date().getFullYear()} ${escapeHTML(resume.name)}
    </div>
  </div>
</body>
</html>`;
}

// HTML 이스케이프 (XSS 방지)
function escapeHTML(text) {
  if (!text) return '';
  return text
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;');
}

// ─── T009-T010: 히어로 섹션 ───

function generateHero(resume, photos) {
  const photoHTML = photos.profilePhoto
    ? `    <img class="hero-photo" src="photos/${encodeURIComponent(photos.profilePhoto)}" alt="${escapeHTML(resume.name)}">\n`
    : '';

  // 한 줄 소개: 첫 번째 경력의 직책 + 회사명
  let subtitle = '';
  if (resume.work.length > 0) {
    const first = resume.work[0];
    subtitle = [first.role, first.company].filter(Boolean).join(' at ');
  }

  const contactParts = [];
  if (resume.email) {
    contactParts.push(`<a href="mailto:${escapeHTML(resume.email)}">${escapeHTML(resume.email)}</a>`);
  }
  if (resume.phone) {
    contactParts.push(escapeHTML(resume.phone));
  }
  const contactHTML = contactParts.length > 0
    ? `      <div class="hero-contact">${contactParts.join(' · ')}</div>\n`
    : '';

  return `    <section class="hero">
${photoHTML}      <h1>${escapeHTML(resume.name)}</h1>
${subtitle ? `      <p class="hero-subtitle">${escapeHTML(subtitle)}</p>\n` : ''}${contactHTML}    </section>
    <hr class="section-divider">`;
}

// ─── T012, T016: 경력 섹션 ───

function generateWorkSection(work) {
  if (!work || work.length === 0) return '';

  const items = work.map(item => {
    const descHTML = item.description
      ? `      <p class="experience-description">${escapeHTML(item.description)}</p>`
      : '';

    return `      <div class="experience-item">
        <div class="experience-header">
          <div>
            <div class="experience-company">${escapeHTML(item.company)}</div>
            <div class="experience-role">${escapeHTML(item.role)}</div>
          </div>
          <div class="experience-meta">
            ${item.location ? escapeHTML(item.location) + '<br>' : ''}${escapeHTML(item.period)}
          </div>
        </div>
${descHTML}
      </div>`;
  }).join('\n');

  return `
    <section>
      <h2>Work Experience</h2>
${items}
    </section>
    <hr class="section-divider">`;
}

// ─── T013: 학력 섹션 ───

function generateEducationSection(education) {
  if (!education || education.length === 0) return '';

  const items = education.map(item => {
    const gpaHTML = item.gpa
      ? `        <div class="education-gpa">${escapeHTML(item.gpa)}</div>`
      : '';

    return `      <div class="education-item">
        <div class="education-header">
          <div>
            <div class="education-degree">${escapeHTML(item.degree)}</div>
            <div class="education-school">${escapeHTML(item.school)}${item.location ? ', ' + escapeHTML(item.location) : ''}</div>
          </div>
          <div class="education-period">${escapeHTML(item.period)}</div>
        </div>
${gpaHTML}
      </div>`;
  }).join('\n');

  return `
    <section>
      <h2>Education</h2>
${items}
    </section>
    <hr class="section-divider">`;
}

// ─── T014: 과외활동 섹션 ───

function generateActivitiesSection(activities) {
  if (!activities || activities.length === 0) return '';

  const items = activities.map(item => {
    const descHTML = item.description
      ? `      <p class="experience-description">${escapeHTML(item.description)}</p>`
      : '';

    return `      <div class="experience-item">
        <div class="experience-header">
          <div>
            <div class="experience-company">${escapeHTML(item.title)}</div>
            <div class="experience-role">${escapeHTML(item.role)}</div>
          </div>
          <div class="experience-meta">
            ${item.location ? escapeHTML(item.location) + '<br>' : ''}${escapeHTML(item.period)}
          </div>
        </div>
${descHTML}
      </div>`;
  }).join('\n');

  return `
    <section>
      <h2>Activities</h2>
${items}
    </section>
    <hr class="section-divider">`;
}

// ─── T015: 기술 섹션 ───

function generateSkillsSection(skills) {
  if (!skills || skills.length === 0) return '';

  const items = skills.map(item => {
    return `      <div class="skill-item">
        <div class="skill-category">${escapeHTML(item.category)}</div>
        <div class="skill-value">${escapeHTML(item.value)}</div>
      </div>`;
  }).join('\n');

  return `
    <section>
      <h2>Skills &amp; Info</h2>
      <div class="skills-grid">
${items}
      </div>
    </section>
    <hr class="section-divider">`;
}

// ─── T018-T019: 갤러리 섹션 ───

function generateGallerySection(photos) {
  if (!photos.galleryPhotos || photos.galleryPhotos.length === 0) return '';

  const items = photos.galleryPhotos.map(file => {
    return `      <div class="gallery-card">
        <img src="photos/${encodeURIComponent(file)}" alt="Photo" loading="lazy">
      </div>`;
  }).join('\n');

  return `
    <section>
      <h2>Gallery</h2>
      <div class="gallery-grid">
${items}
      </div>
    </section>`;
}

// ─── T026: 메인 실행 흐름 ───

function main() {
  console.log('포트폴리오 생성 시작...');

  // ① output/ 정리
  prepareOutputDir();
  console.log('  ✓ output/ 디렉토리 준비 완료');

  // ② 이력서 읽기·파싱
  const resume = readResume();
  console.log(`  ✓ 이력서 파싱 완료: ${resume.name}`);
  console.log(`    - 학력: ${resume.education.length}개`);
  console.log(`    - 경력: ${resume.work.length}개`);
  console.log(`    - 활동: ${resume.activities.length}개`);
  console.log(`    - 기술: ${resume.skills.length}개`);

  // ③ 사진 수집·복사
  const photos = collectPhotos();
  console.log(`  ✓ 사진 ${photos.galleryPhotos.length}장 복사 완료`);

  // ④ HTML 생성
  const html = generateHTML(resume, photos);

  // ⑤ 파일 쓰기
  fs.writeFileSync(path.join(OUTPUT_DIR, 'index.html'), html, 'utf-8');
  console.log('  ✓ output/index.html 생성 완료');

  // ⑥ 완료 메시지
  console.log('\n✅ 포트폴리오 생성 완료!');
  console.log('   output/index.html 파일을 브라우저에서 열어주세요.');
}

main();
