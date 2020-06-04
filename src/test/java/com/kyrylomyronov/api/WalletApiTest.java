package com.kyrylomyronov.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyrylomyronov.leovegastest.entity.TransactionEntity;
import com.kyrylomyronov.leovegastest.entity.WalletEntity;
import com.kyrylomyronov.leovegastest.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static com.kyrylomyronov.leovegastest.exception.error.WalletError.PLAYER_DOES_NOT_EXIST;
import static com.kyrylomyronov.leovegastest.service.impl.TestUtils.createNewTransactionEntity;
import static com.kyrylomyronov.leovegastest.service.impl.TestUtils.createNewWallet;
import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WalletApi.class)
public class WalletApiTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private com.kyrylomyronov.leovegastest.service.WalletService walletService;
	@MockBean
	private com.kyrylomyronov.leovegastest.service.TransactionService transactionService;

	private WalletEntity wallet;

	private static final String WALLETS_URI = "/wallets";

	@BeforeEach
	void setup() {
		wallet = createNewWallet();
	}

	@Test
	void testGetAllWallets() throws Exception {
		List<WalletEntity> wallets = singletonList(wallet);
		when(walletService.getWallets(null)).thenReturn(wallets);

		MvcResult response = mvc.perform(get(WALLETS_URI).contentType(APPLICATION_JSON))
				.andDo(print())        //Get the response printed for better analysis in case of an error.
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.list[0].id").value(wallet.getExternalId()))
				.andExpect(jsonPath("$.list[0].playerId").value(wallet.getPlayerId()))
				.andExpect(jsonPath("$.list[0].balance").value(wallet.getBalance()))
				.andReturn();

		assertEquals(APPLICATION_JSON.toString(), response.getResponse().getContentType());
	}

	@Test
	void testGetWalletForPlayer() throws Exception {
		List<WalletEntity> wallets = singletonList(wallet);
		when(walletService.getWallets("1")).thenReturn(wallets);
		when(walletService.getWallets("2")).thenThrow(new ApiException(PLAYER_DOES_NOT_EXIST));

		mvc.perform(get(WALLETS_URI + "?playerId=1").contentType(APPLICATION_JSON))
				.andDo(print())        //Get the response printed for better visibility.
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.list[0].id").value(wallet.getExternalId()))
				.andExpect(jsonPath("$.list[0].playerId").value(wallet.getPlayerId()))
				.andExpect(jsonPath("$.list[0].balance").value(wallet.getBalance()))
				.andReturn();

		mvc.perform(get(WALLETS_URI + "?playerId=2").contentType(APPLICATION_JSON))
				.andDo(print())       //Get the response printed for better visibility.
				.andExpect(status().isBadRequest())
				.andReturn();
	}


	@Test
	void testUpdateBalance() throws Exception {
		TransactionEntity credit = createNewTransactionEntity(wallet, "credit");
		wallet.setBalance(wallet.getBalance().add(credit.getAmount()));
		wallet.setTransactions(singletonList(credit));
		when(walletService.getWalletByExternalId(wallet.getExternalId())).thenReturn(wallet);
		when(walletService.updateBalance(any(), any())).thenReturn(wallet);
		Object body = new Object() {
			public final String id = "1";
			public final String amount = "2";
			public final String transactionType = "credit";
			public final String note = "First transaction";
		};

		MvcResult response = mvc.perform(post(WALLETS_URI + "/" + wallet.getExternalId() + "/updateBalance")
				.contentType(APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(body)).characterEncoding("utf-8"))
				.andDo(print())        //Get the response printed for better visibility.
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(wallet.getExternalId()))
				.andExpect(jsonPath("$.playerId").value(wallet.getPlayerId()))
				.andExpect(jsonPath("$.balance").value(wallet.getBalance()))
				.andReturn();

		assertEquals(APPLICATION_JSON.toString(), response.getResponse().getContentType());
	}

}
