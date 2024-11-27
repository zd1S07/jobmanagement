package com.example.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.model.PasswordResetToken;
import com.example.app.model.User;
import com.example.app.repository.PasswordResetTokenRepository;
import com.example.app.service.EmailService;
import com.example.app.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/account")
public class AccountController {

	 private final UserService userService;
	    private final PasswordResetTokenRepository passwordResetTokenRepository;
	    private final EmailService emailService; // 追加

	    @Autowired
	    public AccountController(UserService userService, 
	                             PasswordResetTokenRepository passwordResetTokenRepository,
	                             EmailService emailService) { // インジェクト
	        this.userService = userService;
	        this.passwordResetTokenRepository = passwordResetTokenRepository;
	        this.emailService = emailService; // 初期化
	    }
 
    @GetMapping("/create") // 修正: URLを/account/createに変更
    public String createAccountForm(Model model) {
        model.addAttribute("user", new User());
        return "account/create"; // create.htmlにリダイレクト
    }

    @PostMapping("/create")
    public String createAccount(@Valid @ModelAttribute("user") User user, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        // バリデーションエラーをチェック
        if (result.hasErrors()) {
            return "account/create"; // バリデーションエラーがある場合、同じフォームページに戻る
        }

        // メールアドレスが既に登録されているか確認
        if (userService.findUserByEmail(user.getEmail()) != null) {
            result.rejectValue("email", "error.user", "このメールアドレスは既に登録されています");
            return "account/create"; // エラーがあれば同じページを表示
        }

        // ユーザー登録処理
        userService.registerUser(user);

        // フラッシュ属性に成功メッセージを設定
        redirectAttributes.addFlashAttribute("successMessage", "アカウントを作成しました。作成したアカウントでログインしましょう。");

        return "redirect:/account/login"; // 登録後にログインページへリダイレクト
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User()); // フォームのバインドオブジェクトを作成
        return "account/login"; // Thymeleafのテンプレート名
    }
        
 // パスワードリセットリクエストの表示
    @GetMapping("/password/reset")
    public String showPasswordResetForm(Model model) {
        model.addAttribute("email", new String());
        return "account/password_reset";
    }

    // パスワードリセットリクエストの処理
    @PostMapping("/password/reset")
    public String handlePasswordResetRequest(@RequestParam("email") String email, Model model) {
        User user = userService.findUserByEmail(email);
        if (user != null) {
            // リセットトークンを生成してメール送信
            String token = userService.createPasswordResetToken(user);
            emailService.sendPasswordResetEmail(user.getEmail(), token);
            model.addAttribute("message", "パスワードリセットリンクがメールで送信されました");
        } else {
            model.addAttribute("error", "このメールアドレスは登録されていません");
        }
        return "account/password_reset";
    }
    @GetMapping("/password/reset/confirm")
    public String showPasswordResetPage(@RequestParam("token") String token, Model model) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);
        if (resetToken == null || resetToken.isExpired()) {
            model.addAttribute("error", "無効なリセットトークンです");
            return "account/password_reset";
        }
        model.addAttribute("token", token);
        return "account/password_reset_confirm";
    }

    @PostMapping("/password/reset/confirm")
    public String handlePasswordReset(@RequestParam("token") String token, 
                                      @RequestParam("password") String password, 
                                      @RequestParam("confirmPassword") String confirmPassword, 
                                      Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "パスワードが一致しません");
            return "account/password_reset_confirm";
        }

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);
        if (resetToken == null || resetToken.isExpired()) {
            model.addAttribute("error", "無効なリセットトークンです");
            return "account/password_reset_confirm";
        }

        // パスワードをリセット
        User user = resetToken.getUser();
        userService.updatePassword(user, password);
        return "redirect:/account/login?resetSuccess";
    }

    
}
