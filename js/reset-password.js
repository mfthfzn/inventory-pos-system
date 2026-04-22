const toggleNew = document.querySelector(".js-toggle-new");
const newPasswordInput = document.querySelector("#new-password");

toggleNew.addEventListener("click", function () {
  if (newPasswordInput.type === "password") {
    newPasswordInput.type = "text";
    toggleNew.src = "../../assets/show.svg";
  } else {
    newPasswordInput.type = "password";
    toggleNew.src = "../../assets/hide.svg";
  }
});

const toggleConfirm = document.querySelector(".js-toggle-confirm");
const retypeNewPasswordInput = document.querySelector("#confirm-password");

toggleConfirm.addEventListener("click", function () {
  if (retypeNewPasswordInput.type === "password") {
    retypeNewPasswordInput.type = "text";
    toggleConfirm.src = "../../assets/show.svg";
  } else {
    retypeNewPasswordInput.type = "password";
    toggleConfirm.src = "../../assets/hide.svg";
  }
});

document
  .querySelector(".reset-form")
  .addEventListener("submit", async function (event) {
    event.preventDefault();
    const message = document.querySelector(".message");
    const emailValue = document.querySelector("#email");
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const token = urlParams.get("token");

    console.log(token);

    message.textContent = "";

    try {
      const response = await fetch(`http://127.0.0.1:8080/api/auth/reset-password?token=${token}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        body: `new-password=${encodeURIComponent(
          newPasswordInput.value
        )}&retype-new-password=${encodeURIComponent(
          retypeNewPasswordInput.value
        )}`,
        credentials: "include",
      });

      const responseData = await response.json();
      console.log("Response:", responseData);

      if (response.ok) {
        message.textContent = responseData.data.message
      } else {
        message.textContent = responseData.error.message
      }
    } catch (error) {
      console.error("Error:", error);
      message.textContent = "Terjadi kesalahan saat reset email!";
    }
  });
