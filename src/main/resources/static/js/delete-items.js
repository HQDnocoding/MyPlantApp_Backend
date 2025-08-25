
document.addEventListener("DOMContentLoaded", function () {
    const deleteBtn = document.getElementById("deleteBtn");
    const selectAll = document.getElementById("selectAll");
    const checkboxes = document.querySelectorAll(".rowCheckbox");

    // Chọn / bỏ chọn tất cả
    selectAll.addEventListener("change", function () {
        checkboxes.forEach(cb => cb.checked = selectAll.checked);
        toggleDeleteButton();
    });

    // Bật / tắt nút xóa khi chọn checkbox
    checkboxes.forEach(cb => {
        cb.addEventListener("change", toggleDeleteButton);
    });

    function toggleDeleteButton() {
        const selected = Array.from(checkboxes).some(cb => cb.checked);
        deleteBtn.disabled = !selected;
    }

    // Gọi API xóa
    deleteBtn.addEventListener("click", function () {
        const ids = Array.from(checkboxes)
            .filter(cb => cb.checked)
            .map(cb => cb.value);

        if (ids.length === 0) {
            alert("Vui lòng chọn ít nhất một bản ghi để xóa.");
            return;
        }

        if (!confirm(`Bạn có chắc muốn xóa ${ids.length} bản ghi?`)) {
            return;
        }

        fetch("/api/secure/pesticides", {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(ids)
        })
            .then(res => {
                if (res.ok) {
                    alert("Xóa thành công!");
                    location.reload();
                } else {
                    alert("Xóa thất bại!");
                }
            })
            .catch(err => {
                console.error("Lỗi:", err);
                alert("Có lỗi xảy ra khi xóa.");
            });
    });
});
