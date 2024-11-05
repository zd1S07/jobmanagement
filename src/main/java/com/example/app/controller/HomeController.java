package com.example.app.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.model.Company;
import com.example.app.model.FavoriteJob;
import com.example.app.model.Interview;
import com.example.app.model.User;
import com.example.app.repository.CompanyRepository;
import com.example.app.repository.FavoriteJobRepository;
import com.example.app.repository.InterviewRepository;
import com.example.app.repository.UserRepository;
import com.example.app.security.CustomUserDetails;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private FavoriteJobRepository favoriteJobRepository;

	@Autowired
	private InterviewRepository interviewRepository;

	@GetMapping("/")
	public String redirectToAccountCreate() {
		return "redirect:/account/create"; // アカウント作成ページにリダイレクト
	}
	@GetMapping("/home")
	public String showHomePage(Model model, Authentication authentication,
	        @RequestParam(name = "daysUntilNotification", defaultValue = "3") int daysUntilNotification,
	        @RequestParam(name = "sort", defaultValue = "added") String sort,
	        HttpSession session) {
	    // CustomUserDetailsにキャストしてユーザーIDを取得
	    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
	    Long userId = userDetails.getId(); // IDを取得
	    System.out.println("User ID: " + userId);

	    // ユーザーIDをセッションに保存
	    session.setAttribute("userId", userId);

	    // 今日の日付と曜日を取得し、フォーマット
	    LocalDate today = LocalDate.now();
	    String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
	    String dayOfWeek = today.getDayOfWeek().getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.JAPANESE);

	    // モデルに追加
	    model.addAttribute("today", formattedDate);
	    model.addAttribute("dayOfWeek", dayOfWeek);

	    // ユーザーのお気に入り求人を取得
	    List<FavoriteJob> favoriteJobs = favoriteJobRepository.findByUserId(userId);
	    if ("deadline".equals(sort)) {
	        // お気に入り求人を締切日が近い順にソート
	        favoriteJobs.sort(Comparator.comparing(FavoriteJob::getDeadline, Comparator.nullsLast(Comparator.naturalOrder())));
	    }
	    model.addAttribute("favoriteJobs", favoriteJobs);

	    // ユーザーに関連する会社を取得し、面接情報を格納
	    List<Company> userCompanies = companyRepository.findByUserId(userId);
	    List<Interview> upcomingInterviews = new ArrayList<>();
	    LocalDate endDate = LocalDate.now().plusDays(daysUntilNotification);
	    for (Company company : userCompanies) {
	        upcomingInterviews.addAll(interviewRepository.findUpcomingInterviews(company.getId(), endDate));
	    }
	    System.out.println("Upcoming Interviews: " + upcomingInterviews.size());
	    model.addAttribute("upcomingInterviews", upcomingInterviews);

	    // 通知日数オプションと選択値をモデルに追加
	    model.addAttribute("notificationDaysOptions", List.of(1, 3, 5, 7));
	    model.addAttribute("daysUntilNotification", daysUntilNotification);

	    return "home";
	}



	@GetMapping("/logout")
	public String logout() {
		// ログアウト処理をここで実行（実際にはSpring Securityが自動的に処理）
		return "redirect:/account/login?logout"; // ログアウト後にリダイレクト
	}

	@PostMapping("/favoriteJob/add")
	public String addFavoriteJob(@RequestParam String companyName, @RequestParam String jobLink,
			@RequestParam LocalDate deadline, @SessionAttribute("userId") Long userId, // セッションからユーザーIDを取得
			Model model) {

		// お気に入り求人を作成
		FavoriteJob favoriteJob = new FavoriteJob();
		favoriteJob.setCompanyName(companyName);
		favoriteJob.setJobLink(jobLink);
		favoriteJob.setDeadline(deadline);
		favoriteJob.setUserId(userId);

		// お気に入り求人を保存
		favoriteJobRepository.save(favoriteJob);

		// お気に入り求人一覧をモデルに追加
		model.addAttribute("favoriteJobs", favoriteJobRepository.findByUserId(userId));

		return "redirect:/home"; // ホームにリダイレクト
	}

	@GetMapping("/favoriteJob/delete/{jobId}")
	public String deleteFavoriteJob(@PathVariable Long jobId, RedirectAttributes redirectAttributes) {
		// 指定されたIDの求人を削除
		favoriteJobRepository.deleteById(jobId);

		// リダイレクト時に削除完了メッセージを渡す
		redirectAttributes.addFlashAttribute("message", "削除が完了しました。");

		// ホームページにリダイレクト
		return "redirect:/home";
	}

	@GetMapping("/interviewCompany/add")
	public String addCompany(@RequestParam String companyName, RedirectAttributes redirectAttributes) {
		 if (companyRepository.existsByName(companyName)) {
		        redirectAttributes.addFlashAttribute("errorMessage", "この会社は既に追加されています。");
		        return "redirect:/home"; // ホームページにリダイレクト
		    }
		
		// 新しいCompanyオブジェクトを作成
	    Company company = new Company();
	    company.setName(companyName);

	    // 現在ログイン中のユーザーを取得
	    Long userId = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

	    // ユーザーをリポジトリから取得し、会社に設定
	    User user = userRepository.findById(userId)
	                              .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));

	    company.setUser(user); // userを設定

	    // 会社をリポジトリに保存
	    companyRepository.save(company);

	    // リダイレクト時に追加完了メッセージを渡す
	    redirectAttributes.addFlashAttribute("message", "会社情報が追加されました: " + companyName);

	    // company/listにリダイレクト
	    return "redirect:/company/list";
	}
}