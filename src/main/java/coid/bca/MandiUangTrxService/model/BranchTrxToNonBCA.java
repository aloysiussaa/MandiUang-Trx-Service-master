package coid.bca.MandiUangTrxService.model;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "branch_acct_trx_NON_fact")
public class BranchTrxToNonBCA {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trxNonId;
	
	@NotBlank
	private Integer branchAccountId; 
	
	@NotBlank
	private Integer acctRecId;
	
	@NotBlank
	private String transferType;
	
	@NotBlank
	private Integer beneficiaryId;
	
	@NotBlank
	private Double trxAmount;
	
	@NotBlank
	private Date trxDate;
	
	@NotBlank
	private String trxByUser;
	
	@NotBlank
	private String bankName;
}
