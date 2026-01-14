document.addEventListener("DOMContentLoaded", async function (event) {
  event.preventDefault();

  try {
    const response = await fetch(`http://127.0.0.1:8080/api/session`, {
      method: "GET",
      credentials: "include",
    });

    const responseData = await response.json();

    const role = responseData.data.role;
    if(role != "CASHIER") {
      window.location.href = "/app/users/login/";
    }
  } catch (error) {
    console.error("Error:", error);
  }
});
