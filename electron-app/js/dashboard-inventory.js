function updateCloseIcon(searchBar, closeIcon) {
  searchBar.addEventListener("input", () => {
    if (searchBar.value.trim() !== "") {
      closeIcon.style.display = "block";
    } else {
      closeIcon.style.display = "none";
    }
  });

  closeIcon.addEventListener("click", () => {
    searchBar.value = "";
    closeIcon.style.display = "none";
    searchBar.focus();
  });
}

const searchBarDesktop = document.querySelector(".search-bar-desktop .search");
const closeIconDesktop = document.querySelector(
  ".search-bar-desktop .close-icon"
);

const searchBarMobile = document.querySelector(".search-bar-mobile .search");
const closeIconMobile = document.querySelector(
  ".search-bar-mobile .close-icon"
);

updateCloseIcon(searchBarDesktop, closeIconDesktop);
updateCloseIcon(searchBarMobile, closeIconMobile);

// Form overlay-add logic
const overlay = document.getElementById("overlayForm");
const addButton = document.querySelector(".open-form");
const closeBtn = document.getElementById("closeForm");

addButton.addEventListener("click", () => {
  overlay.style.display = "flex";
});

// addButton.onclick = () => (overlay.style.display = "flex");
closeBtn.onclick = () => (overlay.style.display = "none");
overlay.onclick = (e) => {
  if (e.target === overlay) overlay.style.display = "none";
};

// Form overlay-edit logic

document.addEventListener("DOMContentLoaded", () => {
  const overlay = document.getElementById("overlayEdit");
  const stockInput = document.getElementById("stockInput");
  const cancelBtn = document.querySelector(".button-edit-cancel");
  const form = document.getElementById("stockForm");

  let currentProductId = null; // Menyimpan ID produk yang sedang diedit
  let currentStockElement = null; // Elemen teks stok yang akan diperbarui

  document.querySelectorAll(".edit-stock-btn").forEach((button) => {
    button.addEventListener("click", () => {
      currentProductId = button.getAttribute("data-id");

      const card = button.closest(".card");
      currentStockElement = card.querySelector(".stock-value");
      const currentStock = currentStockElement.textContent.trim();

      stockInput.value = currentStock;

      overlay.style.display = "flex";
    });
  });

  cancelBtn.addEventListener("click", () => {
    overlay.style.display = "none";
    currentProductId = null;
  });

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const newStock = stockInput.value.trim();

    if (!currentProductId || newStock === "") {
      alert("Masukkan jumlah stok yang valid!");
      return;
    }

    try {
      const response = await fetch("update-stock.php", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: `id=${encodeURIComponent(
          currentProductId
        )}&stock=${encodeURIComponent(newStock)}`,
      });

      const result = await response.text();

      if (result.includes("success")) {
        currentStockElement.textContent = newStock;
        overlay.style.display = "none";
      } else {
        alert("Gagal memperbarui stok.");
      }
    } catch (err) {
      console.error("Error:", err);
      alert("Terjadi kesalahan saat menyimpan stok.");
    }
  });
});
// End of form overlay-edit logic


// Delete button logic
document.addEventListener("DOMContentLoaded", () => {
  document.querySelectorAll(".delete-btn").forEach(button => {
    button.addEventListener("click", async () => {
      const productId = button.getAttribute("data-id");
      const card = button.closest(".card");

      if (confirm("Apakah kamu yakin ingin menghapus produk ini?")) {
        try {
          const response = await fetch("delete-product.php", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: `id=${encodeURIComponent(productId)}`
          });

          const result = await response.text();

          if (result.includes("success")) {
            card.remove();
            alert("Produk berhasil dihapus!");
          } else {
            alert("Gagal menghapus produk!");
          }
        } catch (err) {
          console.error(err);
          alert("Terjadi kesalahan saat menghapus produk!");
        }
      }
    });
  });
});
// End of delete button logic