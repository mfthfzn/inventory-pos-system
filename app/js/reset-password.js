const toggleConfirm = document.querySelector(".js-toggle-confirm");
const confirmPasswordInput = document.querySelector("#confirm-password");

toggleConfirm.addEventListener("click", function () {
  if (confirmPasswordInput.type === "password") {
    confirmPasswordInput.type = "text";
    toggleConfirm.src = "../../assets/show.svg";
  } else {
    confirmPasswordInput.type = "password";
    toggleConfirm.src = "../../assets/hide.svg";
  }
});

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