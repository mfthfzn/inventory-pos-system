document.addEventListener("DOMContentLoaded", async function (event) {
  event.preventDefault();

  try {
    const response = await fetch(`http://127.0.0.1:8080/api/session`, {
      method: "GET",
      credentials: "include",
    });

    const responseData = await response.json();

    const names = responseData.data.name;
    const role = responseData.data.role;
    const nameOfStore = responseData.data.store_name;

    const profileName = document.querySelector(".profile-name");
    const profileRole = document.querySelector(".profile-role");
    const storeName = document.querySelector(".store-name");

    profileName.textContent = names;
    profileRole.textContent = role;
    storeName.textContent = nameOfStore;
  } catch (error) {
    console.error("Error:", error);
    window.location.href = "/app/users/login/";
  }
});
