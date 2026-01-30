document.addEventListener("DOMContentLoaded", async function (event) {
  event.preventDefault();

  try {
    const response = await fetch(`http://127.0.0.1:8080/api/auth/session`, {
      method: "GET",
      credentials: "include",
    });

    if (response.status === 200) {
      const responseData = await response.json();

      const names = responseData.data.name;
      const role = responseData.data.role;
      const email = responseData.data.email;
      const nameOfStore = responseData.data.store_name;

      const profileName = document.querySelector(".profile-name");
      const profileRole = document.querySelector(".profile-role");
      const storeName = document.querySelector(".store-name");

      profileName.textContent = names;
      profileRole.textContent = role;
      storeName.textContent = nameOfStore;

      const nameField = document.querySelector("#name");
      const emailField = document.querySelector("#email");
      const roleField = document.querySelector("#role");

      if (!nameField || !emailField || !roleField) return;

      nameField.value = names;
      emailField.value = email;
      roleField.value = role;
    } else if (response.status === 401) {
      const responseRefresh = await fetch(`http://127.0.0.1:8080/api/auth/refresh`, {
        method: "GET",
        credentials: "include",
      });

      if(responseRefresh.status === 200) {
        window.location.reload()
      } else {
        window.location.href = "/app/users/login/"
      }

    }
  } catch (error) {
    console.error("Error:", error);
    window.location.href = "/app/users/login/";
  }
});
