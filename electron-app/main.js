const { app, BrowserWindow } = require("electron");

function createWindow() {
  const win = new BrowserWindow({
    width: 1024,
    height: 1024,
    webPreferences: {
      nodeIntegration: true, // agar bisa pakai Node.js di renderer
    },
  });

  win.loadFile("index.html");
}

app.whenReady().then(createWindow);

// Tutup app kalau semua window tertutup
app.on("window-all-closed", () => {
  if (process.platform !== "darwin") app.quit();
});
