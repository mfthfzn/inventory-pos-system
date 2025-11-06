function updateQuantity(cartId, qty) {
  fetch('update-cart.php', {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: 'id=' + encodeURIComponent(cartId) + '&quantity=' + encodeURIComponent(qty)
  })
  .then(res => res.json())
  .then(data => {
    if (data.success) {
      location.reload(); // refresh agar total dan tampilan berubah
    } else {
      alert(data.message);
    }
  });
}

function deleteCart(cartId) {
  if (confirm('Apakah kamu yakin ingin menghapus produk ini dari keranjang?')) {
    fetch('delete-cart.php', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: 'id=' + encodeURIComponent(cartId)
    })
    .then(res => res.json())
    .then(data => {
      if (data.success) {
        location.reload();
      } else {
        alert(data.message);
      }
    });
  }
}

function checkoutCart() {
  if (confirm('Lanjutkan checkout semua item di keranjang?')) {
    fetch('checkout.php', {
      method: 'POST'
    })
    .then(res => res.json())
    .then(data => {
      if (data.success) {
        alert(data.message);
        location.reload();
      } else {
        alert("Gagal: " + data.message);
      }
    });
  }
} 