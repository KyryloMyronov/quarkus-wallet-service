package com.kyrylomyronov.api;

import com.kyrylomyronov.api.model.Transaction;
import com.kyrylomyronov.api.model.Transactions;
import com.kyrylomyronov.api.model.Wallet;
import com.kyrylomyronov.api.model.Wallets;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public interface WalletApi {

	@PostMapping("/wallets")
	Wallet createWallet(@RequestBody Wallet newWallet);

	@GetMapping("/wallets/{id}")
	Wallet getWallet(@PathVariable("id") String id);

	@GetMapping(value = "/wallets")
	Wallets getWallets(@RequestParam(value = "playerId", required = false) String playerId);

	@DeleteMapping("/wallets/{id}")
	void deleteWallet(@PathVariable("id") String id);

	@PostMapping(value = "/wallets/{id}/updateBalance")
	Wallet updateBalance(@PathVariable("id") String id, @RequestBody Transaction newTransaction);

	@GetMapping(value = "/wallets/transactions/{transactionId}")
	Transaction getTransaction(@PathVariable("transactionId") String transactionId);

	@GetMapping("/wallets/{id}/transactions")
	Transactions getWalletTransactions(@PathVariable("id") String id, @RequestParam(value = "type", required = false) String type);

	@GetMapping("/wallets/transactions")
	Transactions getPlayerTransactions(@RequestParam("playerId") String playerId, @RequestParam(value = "type", required = false) String type);
}
