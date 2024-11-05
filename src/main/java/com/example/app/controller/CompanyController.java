package com.example.app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.app.model.Company;
import com.example.app.model.Interview;
import com.example.app.model.User;
import com.example.app.repository.CompanyRepository;
import com.example.app.repository.InterviewRepository;
import com.example.app.repository.UserRepository;
import com.example.app.security.CustomUserDetails;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/company")
public class CompanyController {

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private InterviewRepository interviewRepository;

	@GetMapping("/list")
	public String listCompanies(Model model) {
		// 現在のユーザーを取得
		Long userId = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
		} else {
			// 認証されていない場合、ログインページにリダイレクト
			return "redirect:/account/login";
		}

		// 企業情報をモデルに追加
		List<Company> companies = companyRepository.findByUserId(userId);
		if (companies == null) {
			companies = new ArrayList<>();
		}

		// 企業ごとにインタビューを取得するためのマップを作成
		Map<Long, List<Interview>> companyInterviewsMap = new HashMap<>();
		for (Company company : companies) {
			Long companyId = company.getId();
			List<Interview> interviews = interviewRepository.findByCompanyId(companyId);
			if (interviews == null) {
				interviews = new ArrayList<>(); // 空のリストを追加
			}
			companyInterviewsMap.put(companyId, interviews);
		}

		// companyInterviewsMapをnullでないか確認し、モデルに追加
		if (companyInterviewsMap != null) {
			model.addAttribute("companyInterviewsMap", companyInterviewsMap);
		}

		model.addAttribute("companies", companies);

		return "company/list";
	}

	@PostMapping("/add")
	public String addCompany(@Valid @ModelAttribute Company company, BindingResult result) {
		if (result.hasErrors()) {
			return "company/form";
		}
		// 現在ログイン中のユーザーを取得
		Long userId = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getId();
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));

		// ユーザーと紐付けて保存
		company.setUser(user);
		companyRepository.save(company);

		return "redirect:/company/list";
	}

	@PostMapping("/update")
	public String updateCompany(@Valid @ModelAttribute Company company, BindingResult result) {
		if (result.hasErrors()) {
			return "company/form";
		}

		// 現在のユーザーを取得
		Long userId = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getId();

		// 更新する企業を取得
		Company existingCompany = companyRepository.findById(company.getId())
				.orElseThrow(() -> new RuntimeException("企業が見つかりません"));

		// 企業が現在のユーザーに紐づいているか確認
		if (!existingCompany.getUser().getId().equals(userId)) {
			throw new RuntimeException("この企業を更新する権限がありません");
		}

		// 企業情報を更新
		existingCompany.setName(company.getName());
		existingCompany.setContactPerson(company.getContactPerson());
		existingCompany.setEmail(company.getEmail());
		existingCompany.setPhone(company.getPhone());
		existingCompany.setWebsite(company.getWebsite());

		// 企業情報を保存
		companyRepository.save(existingCompany);

		return "redirect:/company/list";
	}

	@PostMapping("/delete")
	public String deleteCompany(@ModelAttribute Company company) {
		// 現在のユーザーを取得
		Long userId = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getId();

		// 削除する企業を取得
		Company existingCompany = companyRepository.findById(company.getId())
				.orElseThrow(() -> new RuntimeException("企業が見つかりません"));

		// 企業が現在のユーザーに紐づいているか確認
		if (!existingCompany.getUser().getId().equals(userId)) {
			throw new RuntimeException("この企業を削除する権限がありません");
		}

		// 企業情報を削除
		companyRepository.delete(existingCompany);

		return "redirect:/company/list";
	}

	// 面接情報の追加
	@PostMapping("/{companyId}/interview/add")
	public String addInterview(@PathVariable Long companyId, @Valid @ModelAttribute Interview interview,
			BindingResult result, Model model) {
		if (result.hasErrors()) {
			// エラーの場合、会社の情報を再度取得してモデルに追加
			Company company = companyRepository.findById(companyId)
					.orElseThrow(() -> new IllegalArgumentException("Invalid company Id:" + companyId));
			model.addAttribute("company", company);
			return "company/list"; // エラーの場合、企業一覧ページに戻る
		}

		Company company = companyRepository.findById(companyId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid company Id:" + companyId));
		interview.setCompany(company);
		interviewRepository.save(interview);

		return "redirect:/company/list"; // 成功した場合、企業一覧ページにリダイレクト
	}

	// 面接情報の更新
	@PostMapping("/{interviewId}/interview/update")
	public String updateInterview(@PathVariable Long interviewId, @Valid @ModelAttribute Interview interview,
			BindingResult result) {
		if (result.hasErrors()) {
			return "company/list"; // エラーの場合、企業一覧ページに戻る
		}

		Interview existingInterview = interviewRepository.findById(interviewId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid interview Id:" + interviewId));

		existingInterview.setCompany(existingInterview.getCompany()); // 企業情報はそのまま使用
		// 他のフィールドを更新
		existingInterview.setInterviewDate(interview.getInterviewDate());
		existingInterview.setStartTime(interview.getStartTime());
		existingInterview.setEndTime(interview.getEndTime());
		existingInterview.setLocation(interview.getLocation());
		existingInterview.setJobTitle(interview.getJobTitle());
		existingInterview.setSelectionStatus(interview.getSelectionStatus());
		existingInterview.setMotivation(interview.getMotivation());

		interviewRepository.save(existingInterview);

		return "redirect:/company/list"; // 成功した場合、企業一覧ページにリダイレクト
	}

	// 面接情報の削除

	@PostMapping("/interview/{interviewId}/delete")
	public String deleteInterview(@PathVariable Long interviewId) {
		Interview interview = interviewRepository.findById(interviewId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid interview Id:" + interviewId));

		
		try {
			// 面接情報を削除
			interviewRepository.delete(interview);
			System.out.println("Successfully deleted interview with ID: " + interviewId);
		} catch (Exception e) {
			System.out.println("Error deleting interview: " + e.getMessage());
			// エラーが発生した場合、必要に応じてエラーページに遷移したり、エラーメッセージを表示する処理を追加することも可能です。
		}

		return "redirect:/company/list"; // 削除後に企業一覧ページにリダイレクト
	}
}
