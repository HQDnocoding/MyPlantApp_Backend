document.addEventListener("DOMContentLoaded", function () {
    const deleteBtn = document.getElementById("deleteBtn");
    const selectAll = document.getElementById("selectAll");
    const checkboxes = document.querySelectorAll(".rowCheckbox");

    // Chọn/bỏ chọn tất cả
    selectAll.addEventListener("change", () => {
        checkboxes.forEach(cb => cb.checked = selectAll.checked);
        toggleDeleteButton();
    });

    // Bật/tắt nút xóa khi tick
    checkboxes.forEach(cb => cb.addEventListener("change", toggleDeleteButton));

    function toggleDeleteButton() {
        deleteBtn.disabled = !Array.from(checkboxes).some(cb => cb.checked);
    }

    // Xóa nhiều bản ghi
    deleteBtn.addEventListener("click", () => {
        const ids = Array.from(checkboxes)
            .filter(cb => cb.checked)
            .map(cb => cb.value);

        if (ids.length === 0) {
            alert("Vui lòng chọn ít nhất 1 bản ghi để xóa!");
            return;
        }

        if (!confirm(`Bạn có chắc muốn xóa ${ids.length} bản ghi?`)) return;

        fetch("/api/secure/treatments", {
            method: "DELETE",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(ids)
        }).then(res => {
            if (res.ok) {
                alert("Xóa thành công!");
                location.reload();
            } else {
                console.error(res);
                alert("Xóa thất bại!");
            }
        }).catch(err => {
            console.error(err);
            alert("Có lỗi khi xóa!");
        });
    });
});
