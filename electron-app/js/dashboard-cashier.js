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

// === ELEMENTS ===
const overlayCart = document.getElementById("overlayCart");
const productIdInput = document.getElementById("productId");
const quantityInput = document.getElementById("quantityInput");
const cancelBtn = document.querySelector(".button-cart-cancel");

// === BUKA OVERLAY SAAT KLIK "Tambah ke Keranjang" ===
document.querySelectorAll(".to-cart-btn").forEach(button => {
  button.addEventListener("click", () => {
    const productId = button.dataset.id;
    productIdInput.value = productId;
    quantityInput.value = ""; // reset input
    overlayCart.classList.add("active");
  });
});

// === TUTUP OVERLAY SAAT KLIK BATAL ===
cancelBtn.addEventListener("click", () => {
  overlayCart.classList.remove("active");
});

// === SIMPAN KE KERANJANG ===
function addToCart(event) {
  event.preventDefault();
  const productId = productIdInput.value;
  const quantity = quantityInput.value;

  if (!quantity || quantity <= 0) {
    alert("Jumlah barang harus lebih dari 0!");
    return;
  }

  // Kirim data ke PHP (misal: add-to-cart.php)
  fetch("add-to-cart.php", {
    method: "POST",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    body: `product_id=${encodeURIComponent(productId)}&quantity=${encodeURIComponent(quantity)}`
  })
  .then(res => res.text())
  .then(data => {
    alert(data); // tampilkan pesan dari server
    overlayCart.classList.remove("active");
  })
  .catch(err => {
    console.error("Error:", err);
    alert("Gagal menambahkan ke keranjang.");
  });
}

