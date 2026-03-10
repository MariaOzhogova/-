// Элементы управления
const np = document.getElementById('n-p'), rp = document.getElementById('r-p');
const na = document.getElementById('n-a'), ra = document.getElementById('r-a');
const nmax = document.getElementById('n-max'), rmax = document.getElementById('r-max');
const nmin = document.getElementById('n-min'), rmin = document.getElementById('r-min');

// Элементы стенда
const rimP = document.getElementById('rim-p');
const rimA = document.getElementById('rim-a');
const bEnd = document.getElementById('beam-3').querySelector('.beam-core');
const statusText = document.getElementById('status');
const btnClear = document.getElementById('btn-clear');

let historyData = {};
const ctx = document.getElementById('mainChart').getContext('2d');
const chart = new Chart(ctx, {
    type: 'line',
    data: { labels: [], datasets: [{ label: 'I(Δα)', data: [], borderColor: '#3b82f6', backgroundColor: 'rgba(59, 130, 246, 0.15)', borderWidth: 3, fill: true, tension: 0.4, pointRadius: 2 }] },
    options: { interaction: { mode: 'index', intersect: false }, scales: { y: { min: 0, max: 1.1, grid: { color: '#1e293b' } }, x: { grid: { display: false }, title: {display: true, text: 'Разница углов'} } }, plugins: { legend: { display: false } }, animation: false }
});

// Основная физика
function update(recalc = false) {
    let angP = parseFloat(np.value) || 0;
    let angA = parseFloat(na.value) || 0;
    const imax = parseFloat(nmax.value) / 100;
    const imin = parseFloat(nmin.value) / 100;

    let delta = Math.abs(angA - angP) % 180;

    if (recalc) {
        for (let d in historyData) {
            const r = parseFloat(d) * (Math.PI / 180);
            historyData[d] = (imax - imin) * Math.pow(Math.cos(r), 2) + imin;
        }
    }

    const rad = delta * (Math.PI / 180);
    const intensity = (imax - imin) * Math.pow(Math.cos(rad), 2) + imin;
    historyData[delta] = intensity;

    // Вращение
    rimP.style.transform = 'rotate(' + angP + 'deg)';
    rimA.style.transform = 'rotate(' + angA + 'deg)';

    bEnd.style.opacity = intensity;
    document.getElementById('val-i').innerText = Math.round(intensity * 100) + '%';

    // График
    const sorted = Object.keys(historyData).sort((x, y) => parseFloat(x) - parseFloat(y));
    chart.data.labels = sorted;
    chart.data.datasets[0].data = sorted.map(d => historyData[d]);
    chart.update();

    // Статус
    if (delta > 85 && delta < 95) statusText.innerHTML = "🎯 ОСИ СКРЕЩЕНЫ: <span style='color:var(--gold)'>ПРОЕКЦИЯ E = 0</span>";
    else if (delta < 5 || delta > 175) statusText.innerHTML = "🎯 ОСИ ПАРАЛЛЕЛЬНЫ: <span style='color:#22c55e'>МАКСИМУМ ИНТЕНСИВНОСТИ</span>";
    else statusText.innerText = "Угол между фильтрами: " + delta + "°";
}

btnClear.onclick = function() { historyData = {}; update(); };

// Связь ползунков и текстовых полей
function bindInputs(slider, numberInput) {
    slider.addEventListener('input', () => { numberInput.value = slider.value; update(false); });
    numberInput.addEventListener('input', () => { slider.value = numberInput.value; update(false); });
}

function bindGlobal(slider, numberInput) {
    slider.addEventListener('input', () => { numberInput.value = slider.value; update(true); });
    numberInput.addEventListener('input', () => { slider.value = numberInput.value; update(true); });
}

bindInputs(rp, np);
bindInputs(ra, na);
bindGlobal(rmax, nmax);
bindGlobal(rmin, nmin);

// === БЕЗОПАСНАЯ ОТПРАВКА ДАННЫХ (CSRF Защита) ===
function saveLabResultsToServer() {
    const csrfMeta = document.querySelector("meta[name='_csrf']");
    const csrfHeaderMeta = document.querySelector("meta[name='_csrf_header']");

    if (!csrfMeta || !csrfHeaderMeta) {
        alert("⚠️ Токен безопасности CSRF не найден! (Вы запускаете файл локально без Spring Boot сервера?)");
        return;
    }

    const csrfToken = csrfMeta.getAttribute("content");
    const csrfHeader = csrfHeaderMeta.getAttribute("content");

    const dataToSend = { labResults: historyData };
    fetch('/api/save-lab', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify(dataToSend)
    })
        .then(response => {
            if (response.ok) alert("✅ Результаты безопасно сохранены на сервере!");
            else alert("❌ Ошибка сервера (возможно неверный CSRF токен).");
        })
        .catch(error => console.error('Ошибка сети:', error));
}

update();