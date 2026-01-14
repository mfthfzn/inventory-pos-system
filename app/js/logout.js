document
  .getElementById("exitButton")
  .addEventListener("click", async function() {
    try {
      const response = await fetch(
        `http://127.0.0.1:8080/api/session`,
        {
          method: "DELETE",
          headers: {
            'Content-Type': 'application/json',
          },
          credentials: 'include'
        }
      );

      const data = await response.json();

      if (response.status === 200) {
        window.location.href = "/app/users/login";
      }
    } catch (error) {
      console.error("Error:", error);
    }
  });
