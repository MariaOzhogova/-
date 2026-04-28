function renderLabs(container, labs) {
  container.innerHTML = "";

  if (!Array.isArray(labs) || labs.length === 0) {
    container.textContent = "Список лабораторных пока пуст.";
    return;
  }

  for (const lab of labs) {
    const link = document.createElement("a");
    link.className = "lab-link";
    link.href = lab.url;
    link.target = "_blank";

    // noinspection SpellCheckingInspection
    link.rel = "noopener noreferrer";

    const studentName = document.createElement("strong");
    studentName.textContent = lab.student || "Студент";

    const title = document.createElement("span");
    title.textContent = lab.title || "Лабораторная работа";

    link.append(studentName, title);
    container.append(link);
  }
}

async function loadLabs() {
  const container = document.getElementById("labs");
  if (!container) {
    return;
  }

  try {
    const response = await fetch("labs.json", { cache: "no-store" });
    if (!response.ok) {
      container.textContent = "Не удалось загрузить список лабораторных.";
      return;
    }

    const labs = await response.json();
    renderLabs(container, labs);
  } catch {
    container.textContent = "Не удалось загрузить список лабораторных.";
  }
}

void loadLabs();
