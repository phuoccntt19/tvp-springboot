package com.springboot.tvpspringbootheroku.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.springboot.tvpspringbootheroku.dao.UserRepository;
import com.springboot.tvpspringbootheroku.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import net.bytebuddy.utility.RandomString;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
    private JavaMailSender mailSender;
	
	@Autowired
	UserRepository userRepository;

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public User search(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public boolean save(User user, String siteURL) throws UnsupportedEncodingException, MessagingException {
		user.setRole("ROLE_USER");
		user.setEnabled(false);
		
		BCryptPasswordEncoder bPasswordEncoder = new BCryptPasswordEncoder();
		user.setPassword(bPasswordEncoder.encode(user.getPassword()));
		
		String randomCode = RandomString.make(64);
	    user.setVerificationCode(randomCode);
		if(userRepository.findByUsername(user.getUsername()) == null) {
			userRepository.save(user);
			sendVerificationEmail(user, siteURL);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean delete(Long id) {
		try {
			userRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void sendVerificationEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
	    String toAddress = user.getEmail();
	    String fromAddress = "phuoccntt19@gmail.com";
	    String senderName = "Viet Phuoc";
	    String subject = "Please verify your registration";
	    String content = "Dear [[name]],<br>"
	            + "Please click the link below to verify your registration:<br>"
	            + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
	            + "Thank you,<br>"
	            + "Viet Phuoc.";
	     
	    MimeMessage message = mailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message);
	     
	    helper.setFrom(fromAddress, senderName);
	    helper.setTo(toAddress);
	    helper.setSubject(subject);
	     
	    content = content.replace("[[name]]", user.getUsername());
	    String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();
	     
	    content = content.replace("[[URL]]", verifyURL);
	     
	    helper.setText(content, true);
	     
	    mailSender.send(message);
		
	}
	
	public boolean verify(String verificationCode) {
	    User user = userRepository.findByVerificationCode(verificationCode);
	     
	    if (user == null || user.isEnabled()) {
	        return false;
	    } else {
	        user.setVerificationCode(null);
	        user.setEnabled(true);
	        userRepository.save(user);
	        return true;
	    }
	     
	}

}
