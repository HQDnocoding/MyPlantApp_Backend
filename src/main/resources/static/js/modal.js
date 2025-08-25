// Mở modal
function openPlantModal() {
    document.getElementById('plantModal').style.display = 'block';
    searchPlant(1);
}
function openDiseaseModal() {
    document.getElementById('diseaseModal').style.display = 'block';
    searchDisease(1);
}

function openPesticideModal() {
    document.getElementById('pesticideModal').style.display = 'block';
    searchPesticide(1);
}

// Đóng modal
function closePlantModal() {
    document.getElementById('plantModal').style.display = 'none';
}
function closeDiseaseModal() {
    document.getElementById('diseaseModal').style.display = 'none';
}

function closePesticideModal() {
    document.getElementById('pesticideModal').style.display = 'none';
}

// Load & tìm kiếm Plant
function searchPlant(page) {
    const keyword = document.getElementById('plantSearch').value;
    fetch(`/api/secure/plants?page=${page}&size=5&keyword=${keyword}`)
        .then(res => res.json())
        .then(data => {
            const tbody = document.getElementById('plantTableBody');
            tbody.innerHTML = '';
            data.content.forEach(plant => {
                const tr = document.createElement('tr');
                tr.innerHTML = `<td>${plant.id}</td><td>${plant.name}</td>`;
                tr.onclick = () => selectPlant(plant.id, plant.name);
                tbody.appendChild(tr);
            });
            renderPagination('plantPagination', data.totalPages, page, searchPlant);
        });
}

// Load & tìm kiếm Disease
function searchDisease(page) {
    const keyword = document.getElementById('diseaseSearch').value;
    fetch(`/api/secure/diseases?page=${page}&size=5&keyword=${keyword}`)
        .then(res => res.json())
        .then(data => {
            const tbody = document.getElementById('diseaseTableBody');
            tbody.innerHTML = '';
            data.content.forEach(disease => {
                const tr = document.createElement('tr');
                tr.innerHTML = `<td>${disease.id}</td><td>${disease.name}</td>`;
                tr.onclick = () => selectDisease(disease.id, disease.name);
                tbody.appendChild(tr);
            });
            renderPagination('diseasePagination', data.totalPages, page, searchDisease);
        });
}

function searchPesticide(page) {
    const keyword = document.getElementById('pesticideSearch').value;
    fetch(`/api/secure/pesticides?page=${page}&size=5&keyword=${keyword}`)
        .then(res => res.json())
        .then(data => {
            const tbody = document.getElementById('pesticideTableBody');
            tbody.innerHTML = '';
            data.content.forEach(pesticide => {
                const tr = document.createElement('tr');
                tr.innerHTML = `<td>${pesticide.id}</td><td>${pesticide.name}</td>`;
                tr.onclick = () => selectPesticide(pesticide.id, pesticide.name);
                tbody.appendChild(tr);
            });
            renderPagination('pesticidePagination', data.totalPages, page, searchPesticide);
        });
}

// Chọn item
function selectPlant(id, name) {
    document.getElementById('plantId').value = id;
    document.getElementById('plantName').value = name;
    closePlantModal();
}
function selectDisease(id, name) {
    document.getElementById('diseaseId').value = id;
    document.getElementById('diseaseName').value = name;
    closeDiseaseModal();
}
function selectPesticide(id, name) {
    document.getElementById('pesticideId').value = id;
    document.getElementById('pesticideName').value = name;
    closePesticideModal();
}

// Render phân trang
function renderPagination(containerId, totalPages, currentPage, callback) {
    const container = document.getElementById(containerId);
    container.innerHTML = '';

    if (currentPage > 1) {
        const prev = document.createElement('a');
        prev.href = '#';
        prev.textContent = '« Trước';
        prev.onclick = (e) => { e.preventDefault(); callback(currentPage - 1); };
        container.appendChild(prev);
    }

    container.appendChild(document.createTextNode(` Trang ${currentPage}/${totalPages} `));

    if (currentPage < totalPages) {
        const next = document.createElement('a');
        next.href = '#';
        next.textContent = 'Sau »';
        next.onclick = (e) => { e.preventDefault(); callback(currentPage + 1); };
        container.appendChild(next);
    }
}


function debounce(func, delay) {
    let timer;
    return function(...args) {
        clearTimeout(timer);
        timer = setTimeout(() => func.apply(this, args), delay);
    };
}

const searchPlantDebounced = debounce(searchPlant, 300);
const searchDiseaseDebounced = debounce(searchDisease, 300);
const searchPesticideDebounced = debounce(searchPesticide, 300);