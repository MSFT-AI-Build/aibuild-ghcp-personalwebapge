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

  // Navigation items
  const navItems = [];
  if (resume.work.length > 0) navItems.push({ href: '#experience', label: 'Experience' });
  if (resume.education.length > 0) navItems.push({ href: '#education', label: 'Education' });
  if (resume.activities.length > 0) navItems.push({ href: '#activities', label: 'Activities' });
  if (resume.skills.length > 0) navItems.push({ href: '#skills', label: 'Skills' });
  if (photos.galleryPhotos.length > 0) navItems.push({ href: '#gallery', label: 'Gallery' });
  const navHTML = navItems.map(n => `<a href="${n.href}" class="nav-link">${n.label}</a>`).join('\n          ');

  return `<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${escapeHTML(resume.name)} — Portfolio</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,400;0,600;0,700;1,400&family=Outfit:wght@300;400;500;600;700&family=JetBrains+Mono:wght@400;500&display=swap" rel="stylesheet">
  <style>
    /* ─── CSS Variables ─── */
    :root {
      --gold: #E8A817;
      --gold-bright: #F5C518;
      --gold-light: #FFE082;
      --gold-glow: rgba(232, 168, 23, 0.15);
      --gold-glow-strong: rgba(232, 168, 23, 0.3);
      --amber: #D4940A;
      --cream: #FFFDF7;
      --cream-mid: #FFF8E8;
      --warm-white: #FEFCF6;
      --bg: #FAFAF5;
      --card: #FFFFFF;
      --card-hover: #FFFEF9;
      --text-primary: #1A1A18;
      --text-secondary: #4A4A42;
      --text-muted: #8A897E;
      --border: #EDE8D8;
      --border-light: #F5F0E3;
      --shadow-sm: 0 1px 3px rgba(26,26,24,0.04), 0 1px 2px rgba(26,26,24,0.02);
      --shadow-md: 0 4px 16px rgba(26,26,24,0.06), 0 2px 6px rgba(26,26,24,0.03);
      --shadow-lg: 0 12px 40px rgba(26,26,24,0.08), 0 4px 12px rgba(26,26,24,0.04);
      --shadow-gold: 0 8px 32px rgba(232,168,23,0.12), 0 2px 8px rgba(232,168,23,0.06);
      --radius: 16px;
      --radius-sm: 10px;
      --font-display: 'Cormorant Garamond', Georgia, serif;
      --font-body: 'Outfit', system-ui, sans-serif;
      --font-mono: 'JetBrains Mono', monospace;
      --transition: 0.35s cubic-bezier(0.25, 0.46, 0.45, 0.94);
      --transition-fast: 0.2s cubic-bezier(0.25, 0.46, 0.45, 0.94);
    }

    /* ─── Reset & Global ─── */
    *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
    html { scroll-behavior: smooth; font-size: 16px; }

    body {
      font-family: var(--font-body);
      line-height: 1.7;
      color: var(--text-primary);
      background: var(--bg);
      -webkit-font-smoothing: antialiased;
      -moz-osx-font-smoothing: grayscale;
      overflow-x: hidden;
    }

    /* Subtle grain overlay */
    body::before {
      content: '';
      position: fixed;
      inset: 0;
      pointer-events: none;
      z-index: 9999;
      opacity: 0.015;
      background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 256 256' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='n'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.9' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23n)'/%3E%3C/svg%3E");
    }

    ::selection {
      background: var(--gold-light);
      color: var(--text-primary);
    }

    /* ─── Navigation ─── */
    .nav {
      position: sticky;
      top: 0;
      z-index: 100;
      background: rgba(250, 250, 245, 0.85);
      backdrop-filter: blur(20px) saturate(1.2);
      -webkit-backdrop-filter: blur(20px) saturate(1.2);
      border-bottom: 1px solid var(--border-light);
      transition: box-shadow var(--transition);
    }
    .nav.scrolled {
      box-shadow: 0 1px 12px rgba(0,0,0,0.04);
    }
    .nav-inner {
      max-width: 1100px;
      margin: 0 auto;
      padding: 0 40px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      height: 64px;
    }
    .nav-brand {
      font-family: var(--font-display);
      font-size: 1.35rem;
      font-weight: 700;
      color: var(--text-primary);
      text-decoration: none;
      letter-spacing: -0.02em;
    }
    .nav-brand span {
      color: var(--gold);
    }
    .nav-links {
      display: flex;
      align-items: center;
      gap: 8px;
    }
    .nav-link {
      font-family: var(--font-body);
      font-size: 0.82rem;
      font-weight: 500;
      color: var(--text-muted);
      text-decoration: none;
      padding: 6px 16px;
      border-radius: 100px;
      letter-spacing: 0.03em;
      text-transform: uppercase;
      transition: all var(--transition-fast);
      position: relative;
    }
    .nav-link:hover {
      color: var(--text-primary);
      background: var(--gold-glow);
    }
    .nav-link:active {
      transform: scale(0.97);
    }

    /* ─── Layout ─── */
    .container {
      max-width: 1100px;
      margin: 0 auto;
      padding: 0 40px;
    }

    section {
      padding: 80px 0;
      position: relative;
    }

    /* ─── Section Header ─── */
    .section-label {
      display: flex;
      align-items: center;
      gap: 16px;
      margin-bottom: 48px;
    }
    .section-label::before {
      content: '';
      width: 40px;
      height: 2px;
      background: linear-gradient(90deg, var(--gold), var(--gold-bright));
      border-radius: 2px;
      flex-shrink: 0;
    }
    .section-label h2 {
      font-family: var(--font-display);
      font-size: 2.2rem;
      font-weight: 700;
      color: var(--text-primary);
      letter-spacing: -0.03em;
      line-height: 1.1;
    }
    .section-label .section-count {
      font-family: var(--font-mono);
      font-size: 0.75rem;
      color: var(--text-muted);
      background: var(--cream-mid);
      padding: 3px 10px;
      border-radius: 100px;
      margin-left: 8px;
    }

    /* ─── Hero ─── */
    .hero {
      padding: 100px 0 80px;
      text-align: center;
      position: relative;
    }
    .hero::before {
      content: '';
      position: absolute;
      top: -20%;
      left: 50%;
      transform: translateX(-50%);
      width: 600px;
      height: 600px;
      border-radius: 50%;
      background: radial-gradient(circle, var(--gold-glow) 0%, transparent 70%);
      pointer-events: none;
      z-index: 0;
    }
    .hero > * {
      position: relative;
      z-index: 1;
    }
    .hero-photo-wrap {
      display: inline-block;
      position: relative;
      margin-bottom: 32px;
    }
    .hero-photo-wrap::before {
      content: '';
      position: absolute;
      inset: -6px;
      border-radius: 50%;
      background: linear-gradient(135deg, var(--gold-bright), var(--amber), var(--gold-light));
      z-index: 0;
      animation: ring-rotate 8s linear infinite;
    }
    @keyframes ring-rotate {
      0% { background: linear-gradient(0deg, var(--gold-bright), var(--amber), var(--gold-light)); }
      25% { background: linear-gradient(90deg, var(--gold-bright), var(--amber), var(--gold-light)); }
      50% { background: linear-gradient(180deg, var(--gold-bright), var(--amber), var(--gold-light)); }
      75% { background: linear-gradient(270deg, var(--gold-bright), var(--amber), var(--gold-light)); }
      100% { background: linear-gradient(360deg, var(--gold-bright), var(--amber), var(--gold-light)); }
    }
    .hero-photo {
      position: relative;
      z-index: 1;
      width: 170px;
      height: 170px;
      border-radius: 50%;
      object-fit: cover;
      border: 4px solid var(--cream);
      display: block;
    }
    .hero h1 {
      font-family: var(--font-display);
      font-size: 3.8rem;
      font-weight: 700;
      color: var(--text-primary);
      letter-spacing: -0.04em;
      line-height: 1.05;
      margin-bottom: 12px;
    }
    .hero-subtitle {
      font-family: var(--font-body);
      font-size: 1.15rem;
      font-weight: 400;
      color: var(--text-secondary);
      letter-spacing: 0.01em;
    }
    .hero-subtitle .highlight {
      color: var(--gold);
      font-weight: 600;
    }
    .hero-contact {
      margin-top: 28px;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 12px;
      flex-wrap: wrap;
    }
    .hero-contact a, .hero-contact span {
      font-family: var(--font-mono);
      font-size: 0.82rem;
      color: var(--text-muted);
      text-decoration: none;
      padding: 8px 20px;
      border: 1px solid var(--border);
      border-radius: 100px;
      transition: all var(--transition);
      letter-spacing: 0.01em;
    }
    .hero-contact a:hover {
      color: var(--amber);
      border-color: var(--gold);
      background: var(--gold-glow);
      transform: translateY(-2px);
      box-shadow: var(--shadow-gold);
    }

    /* ─── Divider ─── */
    .section-divider {
      border: none;
      height: 1px;
      background: linear-gradient(90deg, transparent, var(--border), var(--gold-light), var(--border), transparent);
      margin: 0;
    }

    /* ─── Experience / Activity Cards ─── */
    .experience-item {
      position: relative;
      margin-bottom: 24px;
      padding: 32px 36px;
      background: var(--card);
      border-radius: var(--radius);
      border: 1px solid var(--border-light);
      box-shadow: var(--shadow-sm);
      transition: all var(--transition);
      overflow: hidden;
    }
    .experience-item::before {
      content: '';
      position: absolute;
      left: 0;
      top: 0;
      bottom: 0;
      width: 3px;
      background: linear-gradient(180deg, var(--gold), var(--gold-bright));
      border-radius: 0 2px 2px 0;
      opacity: 0;
      transition: opacity var(--transition);
    }
    .experience-item:hover {
      border-color: var(--gold-light);
      box-shadow: var(--shadow-lg);
      transform: translateY(-3px);
      background: var(--card-hover);
    }
    .experience-item:hover::before {
      opacity: 1;
    }
    .experience-header {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      flex-wrap: wrap;
      gap: 12px;
      margin-bottom: 12px;
    }
    .experience-company {
      font-family: var(--font-display);
      font-size: 1.4rem;
      font-weight: 700;
      color: var(--text-primary);
      letter-spacing: -0.02em;
    }
    .experience-role {
      font-family: var(--font-body);
      font-size: 0.92rem;
      font-weight: 500;
      color: var(--gold);
      margin-top: 2px;
    }
    .experience-meta {
      font-family: var(--font-mono);
      font-size: 0.78rem;
      color: var(--text-muted);
      text-align: right;
      white-space: nowrap;
      background: var(--cream-mid);
      padding: 6px 14px;
      border-radius: var(--radius-sm);
      line-height: 1.6;
    }
    .experience-description {
      font-size: 0.92rem;
      color: var(--text-secondary);
      line-height: 1.8;
      margin-top: 16px;
    }

    /* ─── Education Cards ─── */
    .education-item {
      margin-bottom: 20px;
      padding: 28px 32px;
      background: var(--card);
      border-radius: var(--radius);
      border: 1px solid var(--border-light);
      box-shadow: var(--shadow-sm);
      transition: all var(--transition);
      position: relative;
      overflow: hidden;
    }
    .education-item::after {
      content: '';
      position: absolute;
      right: 24px;
      top: 50%;
      transform: translateY(-50%);
      width: 48px;
      height: 48px;
      border-radius: 50%;
      background: var(--gold-glow);
      opacity: 0;
      transition: opacity var(--transition);
    }
    .education-item:hover {
      border-color: var(--gold-light);
      box-shadow: var(--shadow-md);
      transform: translateY(-2px);
    }
    .education-item:hover::after {
      opacity: 1;
    }
    .education-header {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      flex-wrap: wrap;
      gap: 12px;
    }
    .education-degree {
      font-family: var(--font-display);
      font-size: 1.25rem;
      font-weight: 700;
      color: var(--text-primary);
      letter-spacing: -0.01em;
    }
    .education-school {
      font-size: 0.9rem;
      color: var(--text-secondary);
      margin-top: 2px;
    }
    .education-period {
      font-family: var(--font-mono);
      font-size: 0.78rem;
      color: var(--text-muted);
      white-space: nowrap;
      background: var(--cream-mid);
      padding: 4px 12px;
      border-radius: 100px;
      align-self: flex-start;
    }
    .education-gpa {
      font-family: var(--font-mono);
      font-size: 0.82rem;
      color: var(--gold);
      margin-top: 10px;
      font-weight: 500;
    }

    /* ─── Skills ─── */
    .skills-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
      gap: 20px;
    }
    .skill-item {
      padding: 28px 32px;
      background: var(--card);
      border-radius: var(--radius);
      border: 1px solid var(--border-light);
      box-shadow: var(--shadow-sm);
      transition: all var(--transition);
      position: relative;
    }
    .skill-item:hover {
      border-color: var(--gold-light);
      box-shadow: var(--shadow-md);
      transform: translateY(-2px);
    }
    .skill-category {
      font-family: var(--font-mono);
      font-size: 0.72rem;
      font-weight: 500;
      color: var(--gold);
      text-transform: uppercase;
      letter-spacing: 0.1em;
      margin-bottom: 8px;
    }
    .skill-value {
      font-size: 0.95rem;
      color: var(--text-secondary);
      line-height: 1.7;
    }

    /* ─── Gallery ─── */
    .gallery-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
      gap: 20px;
    }
    .gallery-card {
      position: relative;
      overflow: hidden;
      border-radius: var(--radius);
      box-shadow: var(--shadow-sm);
      border: 1px solid var(--border-light);
      cursor: pointer;
      transition: all var(--transition);
    }
    .gallery-card::after {
      content: '';
      position: absolute;
      inset: 0;
      background: linear-gradient(180deg, transparent 40%, rgba(26,26,24,0.5) 100%);
      opacity: 0;
      transition: opacity var(--transition);
    }
    .gallery-card:hover {
      transform: translateY(-5px) scale(1.02);
      box-shadow: var(--shadow-lg);
      border-color: var(--gold-light);
    }
    .gallery-card:hover::after {
      opacity: 1;
    }
    .gallery-card img {
      width: 100%;
      height: 280px;
      object-fit: cover;
      display: block;
      transition: transform 0.6s cubic-bezier(0.25, 0.46, 0.45, 0.94);
    }
    .gallery-card:hover img {
      transform: scale(1.08);
    }

    /* ─── Footer ─── */
    .footer {
      text-align: center;
      padding: 48px 0;
      font-family: var(--font-mono);
      font-size: 0.78rem;
      color: var(--text-muted);
      letter-spacing: 0.05em;
    }
    .footer-accent {
      display: inline-block;
      width: 24px;
      height: 2px;
      background: var(--gold);
      border-radius: 2px;
      margin-bottom: 16px;
    }

    /* ─── Scroll Animations ─── */
    .reveal {
      opacity: 0;
      transform: translateY(32px);
      transition: opacity 0.7s cubic-bezier(0.25, 0.46, 0.45, 0.94),
                  transform 0.7s cubic-bezier(0.25, 0.46, 0.45, 0.94);
    }
    .reveal.visible {
      opacity: 1;
      transform: translateY(0);
    }

    /* Stagger children */
    .stagger > .reveal:nth-child(1) { transition-delay: 0s; }
    .stagger > .reveal:nth-child(2) { transition-delay: 0.08s; }
    .stagger > .reveal:nth-child(3) { transition-delay: 0.16s; }
    .stagger > .reveal:nth-child(4) { transition-delay: 0.24s; }
    .stagger > .reveal:nth-child(5) { transition-delay: 0.32s; }
    .stagger > .reveal:nth-child(6) { transition-delay: 0.4s; }

    /* Hero entrance */
    .hero-enter {
      opacity: 0;
      transform: translateY(24px);
      animation: hero-fade 0.9s cubic-bezier(0.25, 0.46, 0.45, 0.94) forwards;
    }
    .hero-enter:nth-child(1) { animation-delay: 0.1s; }
    .hero-enter:nth-child(2) { animation-delay: 0.25s; }
    .hero-enter:nth-child(3) { animation-delay: 0.4s; }
    .hero-enter:nth-child(4) { animation-delay: 0.55s; }
    @keyframes hero-fade {
      to { opacity: 1; transform: translateY(0); }
    }

    /* ─── Responsive ─── */
    @media (max-width: 768px) {
      .container { padding: 0 20px; }
      .nav-inner { padding: 0 20px; }
      .nav-links { display: none; }
      section { padding: 56px 0; }
      .hero { padding: 72px 0 56px; }
      .hero h1 { font-size: 2.6rem; }
      .hero::before { width: 300px; height: 300px; }
      .section-label h2 { font-size: 1.7rem; }
      .experience-item { padding: 24px; }
      .experience-header { flex-direction: column; }
      .experience-meta { text-align: left; align-self: flex-start; }
      .education-header { flex-direction: column; }
      .education-item { padding: 20px 24px; }
      .gallery-grid { grid-template-columns: 1fr; }
      .skills-grid { grid-template-columns: 1fr; }
      .gallery-card img { height: 220px; }
    }

    @media (max-width: 480px) {
      .hero h1 { font-size: 2.1rem; }
      .hero-photo { width: 130px; height: 130px; }
      .hero-photo-wrap::before { inset: -5px; }
      .experience-company { font-size: 1.2rem; }
    }
  </style>
</head>
<body>
  <nav class="nav" id="nav">
    <div class="nav-inner">
      <a href="#" class="nav-brand">${escapeHTML(resume.name.split(' ')[0])}<span>.</span></a>
      <div class="nav-links">
        ${navHTML}
      </div>
    </div>
  </nav>

  <div class="container">
${heroSection}
${workSection}
${educationSection}
${activitiesSection}
${skillsSection}
${gallerySection}
    <div class="footer">
      <div class="footer-accent"></div><br>
      &copy; ${new Date().getFullYear()} ${escapeHTML(resume.name)} &mdash; Crafted with intention
    </div>
  </div>

  <script>
    // Scroll reveal
    const observer = new IntersectionObserver((entries) => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          entry.target.classList.add('visible');
        }
      });
    }, { threshold: 0.08, rootMargin: '0px 0px -40px 0px' });
    document.querySelectorAll('.reveal').forEach(el => observer.observe(el));

    // Nav shadow on scroll
    const nav = document.getElementById('nav');
    window.addEventListener('scroll', () => {
      nav.classList.toggle('scrolled', window.scrollY > 20);
    }, { passive: true });
  </script>
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
    ? `      <div class="hero-photo-wrap hero-enter">\n        <img class="hero-photo" src="photos/${encodeURIComponent(photos.profilePhoto)}" alt="${escapeHTML(resume.name)}">\n      </div>\n`
    : '';

  // 한 줄 소개: 첫 번째 경력의 직책 + 회사명
  let subtitle = '';
  if (resume.work.length > 0) {
    const first = resume.work[0];
    subtitle = `${escapeHTML(first.role)} at <span class="highlight">${escapeHTML(first.company)}</span>`;
  }

  const contactParts = [];
  if (resume.email) {
    contactParts.push(`<a href="mailto:${escapeHTML(resume.email)}">${escapeHTML(resume.email)}</a>`);
  }
  if (resume.phone) {
    contactParts.push(`<span>${escapeHTML(resume.phone)}</span>`);
  }
  const contactHTML = contactParts.length > 0
    ? `      <div class="hero-contact hero-enter">${contactParts.join('\n        ')}</div>\n`
    : '';

  return `    <section class="hero">
${photoHTML}      <h1 class="hero-enter">${escapeHTML(resume.name)}</h1>
${subtitle ? `      <p class="hero-subtitle hero-enter">${subtitle}</p>\n` : ''}${contactHTML}    </section>
    <hr class="section-divider">`;
}

// ─── T012, T016: 경력 섹션 ───

function generateWorkSection(work) {
  if (!work || work.length === 0) return '';

  const items = work.map(item => {
    const descHTML = item.description
      ? `      <p class="experience-description">${escapeHTML(item.description)}</p>`
      : '';

    return `      <div class="experience-item reveal">
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
    <section id="experience">
      <div class="section-label reveal"><h2>Work Experience</h2><span class="section-count">${work.length}</span></div>
      <div class="stagger">
${items}
      </div>
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

    return `      <div class="education-item reveal">
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
    <section id="education">
      <div class="section-label reveal"><h2>Education</h2></div>
      <div class="stagger">
${items}
      </div>
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

    return `      <div class="experience-item reveal">
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
    <section id="activities">
      <div class="section-label reveal"><h2>Activities</h2></div>
      <div class="stagger">
${items}
      </div>
    </section>
    <hr class="section-divider">`;  
}

// ─── T015: 기술 섹션 ───

function generateSkillsSection(skills) {
  if (!skills || skills.length === 0) return '';

  const items = skills.map(item => {
    return `      <div class="skill-item reveal">
        <div class="skill-category">${escapeHTML(item.category)}</div>
        <div class="skill-value">${escapeHTML(item.value)}</div>
      </div>`;
  }).join('\n');

  return `
    <section id="skills">
      <div class="section-label reveal"><h2>Skills &amp; Info</h2></div>
      <div class="skills-grid stagger">
${items}
      </div>
    </section>
    <hr class="section-divider">`;  
}

// ─── T018-T019: 갤러리 섹션 ───

function generateGallerySection(photos) {
  if (!photos.galleryPhotos || photos.galleryPhotos.length === 0) return '';

  const items = photos.galleryPhotos.map(file => {
    return `      <div class="gallery-card reveal">
        <img src="photos/${encodeURIComponent(file)}" alt="Photo" loading="lazy">
      </div>`;
  }).join('\n');

  return `
    <section id="gallery">
      <div class="section-label reveal"><h2>Gallery</h2><span class="section-count">${photos.galleryPhotos.length}</span></div>
      <div class="gallery-grid stagger">
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
