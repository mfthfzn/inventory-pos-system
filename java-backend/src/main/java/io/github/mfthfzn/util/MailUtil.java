package io.github.mfthfzn.util;

public class MailUtil {

  public static String getResetPasswordTemplate(String name, String link) {
    String template = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Reset Password</title>
                <style>
                    body { margin: 0; padding: 0; min-width: 100%; font-family: Arial, sans-serif; font-size: 16px; line-height: 1.5; color: #333333; }
                    a { color: #007bff; text-decoration: none; }
                    h1 { font-size: 24px; font-weight: bold; color: #333333; margin-bottom: 20px; }
                    .btn { display: inline-block; padding: 12px 24px; background-color: #007bff; color: #ffffff !important; text-decoration: none; border-radius: 4px; font-weight: bold; }
                    .footer { font-size: 12px; color: #999999; margin-top: 30px; text-align: center; }
                </style>
            </head>
            <body style="background-color: #f4f4f4;">
            
                <table width="100%" border="0" cellpadding="0" cellspacing="0" style="background-color: #f4f4f4; padding: 20px;">
                    <tr>
                        <td align="center">
            
                            <table width="100%" border="0" cellpadding="0" cellspacing="0" style="max-width: 600px; background-color: #ffffff; padding: 40px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);">
                                <tr>
                                    <td>
                                        <h1>Lupa Password?</h1>
            
                                        <p>Halo <strong>{{NAME}}</strong>,</p>
            
                                        <p>Kami menerima permintaan untuk mereset password akun Anda. Jangan khawatir, Anda bisa membuat password baru dengan menekan tombol di bawah ini:</p>
            
                                        <table width="100%" border="0" cellpadding="0" cellspacing="0" style="margin: 30px 0;">
                                            <tr>
                                                <td align="center">
                                                    <a href="{{LINK}}" class="btn">Reset Password Saya</a>
                                                </td>
                                            </tr>
                                        </table>
            
                                        <p style="font-size: 14px; color: #666;">Link ini hanya berlaku selama <strong>15 menit</strong>.</p>
            
                                        <hr style="border: none; border-top: 1px solid #eee; margin: 20px 0;">
            
                                        <p style="font-size: 13px; color: #666;">
                                            Jika tombol di atas tidak berfungsi, salin dan tempel link berikut ke browser Anda:<br>
                                            <a href="{{LINK}}" style="word-break: break-all;">{{LINK}}</a>
                                        </p>
            
                                        <p style="font-size: 13px; color: #999;">
                                            Jika Anda tidak merasa meminta reset password, abaikan email ini. Akun Anda tetap aman.
                                        </p>
                                    </td>
                                </tr>
                            </table>
            
                            <div class="footer">
                                <p>&copy; 2024 Nama Aplikasi Anda. All rights reserved.</p>
                            </div>
            
                        </td>
                    </tr>
                </table>
            
            </body>
            </html>
            """;
    return template
            .replace("{{NAME}}", name)
            .replace("{{LINK}}", link);
  }

}
