import { getCookie } from "./cookie.js";

document.addEventListener("DOMContentLoaded", function () {
  let name = getCookie("name");
  let role = getCookie("role");

  const profileName = document.querySelector(".profile-name");
  const profileRole = document.querySelector(".profile-role");

  profileName.textContent = name;
  profileRole.textContent = role;
});