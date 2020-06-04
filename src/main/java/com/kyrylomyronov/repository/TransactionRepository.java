package com.kyrylomyronov.repository;

import java.util.List;
import java.util.Optional;

import com.kyrylomyronov.entity.TransactionEntity;
import com.kyrylomyronov.entity.TransactionType;
import com.kyrylomyronov.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {

	@Query("SELECT t FROM TransactionEntity t " +
			"WHERE t.wallet.externalId = :walletExternalId AND (:transactionType IS NULL OR t.transactionType = :transactionType)" +
			"ORDER BY t.modified DESC")
	List<TransactionEntity> getAllByWalletIdAndTransactionType(String walletExternalId, TransactionType transactionType);

	@Query("SELECT t FROM TransactionEntity t " +
			"WHERE t.wallet = :wallet AND (:transactionType IS NULL OR t.transactionType = :transactionType)" +
			"ORDER BY t.modified DESC")
	List<TransactionEntity> getAllByWalletAndTransactionType(WalletEntity wallet, TransactionType transactionType);

	Optional<TransactionEntity> getByExternalId(String externalId);

	@Query("SELECT t FROM TransactionEntity t " +
			"WHERE t.wallet.playerId = :playerId AND (:type IS NULL OR t.transactionType = :type)" +
			"ORDER BY t.modified DESC")
	List<TransactionEntity> getAllTransactions(@Param("playerId") String playerId, @Param("type") TransactionType type);
}
