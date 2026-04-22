document
  .querySelector(".forgot-password-form")
  .addEventListener("submit", async function (event) {
    event.preventDefault();
    const message = document.querySelector(".message");
    const emailValue = document.querySelector("#email");

    message.textContent = "";

    try {
      const response = await fetch("http://127.0.0.1:8080/api/auth/forgot-password", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        body: `email=${encodeURIComponent(
          emailValue.value
        )}`,
        credentials: "include",
      });

      const responseData = await response.json();
      console.log("Response:", responseData);

      message.textContent = "Berhasil mengirim link via email Anda!"
    } catch (error) {
      console.error("Error:", error);
      message.textContent = "Terjadi kesalahan saat mengirimkan email!";
    }
  });