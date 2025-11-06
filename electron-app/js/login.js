const visibilityLogo = document.querySelector(".visibility-logo");
const passwordField = document.querySelector("#password");

visibilityLogo.addEventListener("click", function() {
  if (passwordField.type == "password") {
    passwordField.type = "text";
    visibilityLogo.src = "assets/show.svg";
  } else {
    passwordField.type = "password";
    visibilityLogo.src = "assets/hide.svg";
  }
})