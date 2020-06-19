package coid.bca.MandiUangTrxService.controller;

import java.net.URI;
import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import coid.bca.MandiUangTrxService.repository.UserRepository;
import coid.bca.MandiUangTrxService.model.BranchTrxToBCA;
import coid.bca.MandiUangTrxService.model.User;
import coid.bca.MandiUangTrxService.payload.ApiResponse;
import coid.bca.MandiUangTrxService.payload.TrxToBCARequest;
import coid.bca.MandiUangTrxService.repository.TransBCARepository;


@RestController
@RequestMapping("/v1/transactionBranch")
public class BranchTrx {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
    TransBCARepository transBCARepository;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/transToBCA")
    public ResponseEntity<?> createTrans(@Valid @RequestBody TrxToBCARequest newTrxBCARequest){
		Date dateStarted = new Date();
		String verCode = "12345678";
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
    	Optional<User> userOpt = userRepository.findByUsername(userDetail.getUsername()); 
    	User user = userOpt.get();
    	if (!verCode.equals(newTrxBCARequest.getVerificationCode())) {
    		return new ResponseEntity(new ApiResponse(false, "Verification Code False"),
                    HttpStatus.BAD_REQUEST);
    	}
		BranchTrxToBCA transToBCA = new BranchTrxToBCA(newTrxBCARequest.getAcctRecNo(), user.getBranchIdWork(), newTrxBCARequest.getTransferType(), 
				newTrxBCARequest.getBeneficiaryId(), newTrxBCARequest.getTrxAmount(), dateStarted, user.getUsername());
		
		BranchTrxToBCA result = transBCARepository.save(transToBCA);
        URI location = ServletUriComponentsBuilder
        		.fromCurrentContextPath().path("/v1/transactionBranch")
                .buildAndExpand(result.getTrxId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "Transaction Success!"));
	}
	
}

