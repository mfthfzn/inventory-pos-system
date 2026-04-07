const visibilityLogo = document.querySelector(".visibility-logo");
const password = document.querySelector("#password");

visibilityLogo.addEventListener("click", function () {
  if (password.type == "password") {
    password.type = "text";
    visibilityLogo.src = "../../assets/show.svg";
  } else {
    password.type = "password";
    visibilityLogo.src = "../../assets/hide.svg";
  }
});

const emailValue = document.getElementById("email");
const passwordValue = document.getElementById("password");

document
  .querySelector(".login-form")
  .addEventListener("submit", async function (event) {
    event.preventDefault();
    const messageError = document.querySelector(".message-error");

    messageError.textContent = "";

    try {
      const response = await fetch("http://127.0.0.1:8080/api/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        body: `email=${encodeURIComponent(
          emailValue.value
        )}&password=${encodeURIComponent(passwordValue.value)}`,
        credentials: "include",
      });

      const responseData = await response.json();
      console.log("Response:", responseData);

      if (response.status === 200 && responseData.data.role == "CASHIER") {
        window.location.href = "/app/users/cashiers/dashboard/";
      } else if (
        response.status === 200 &&
        responseData.data.role == "INVENTORY_STAFF"
      ) {
        window.location.href = "/app/users/inventory/dashboard/";
      } else if (
        response.status === 200 &&
        responseData.data.role == "MANAGER"
      ) {
        window.location.href = "/app/users/manager/dashboard/";
      } else if (response.status === 302) {
        window.location.reload();
      } else {
        messageError.textContent = responseData.error.message;
        // emailValue.value = "";
        // passwordValue.value = "";
      }
    } catch (error) {
      console.error("Error:", error);
      messageError.textContent = "Terjadi kesalahan saat login!";
    }
  });

document.addEventListener("DOMContentLoaded", async function (event) {
  try {
    const response = await fetch(`http://127.0.0.1:8080/api/auth/session`, {
      method: "GET",
      credentials: "include",
    });

    if (response.status == 200) {
      const responseData = await response.json();
      if (responseData.data.role == "CASHIER") {
        window.location.href = "/app/users/cashiers/dashboard/";
      } else if (responseData.data.role == "INVENTORY_STAFF") {
        window.location.href = "/app/users/inventory/dashboard/";
      } else if (responseData.data.role == "MANAGER") {
        window.location.href = "/app/users/manager/dashboard/";
      }
    }
  } catch (error) {
    console.error("Error:", error);
  }
});
