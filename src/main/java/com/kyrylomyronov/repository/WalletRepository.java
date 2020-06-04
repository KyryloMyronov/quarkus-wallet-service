package com.kyrylomyronov.repository;

import java.util.List;
import java.util.Optional;

import com.kyrylomyronov.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<WalletEntity, String> {
	List<WalletEntity> getByPlayerId(String playerId);

	Optional<WalletEntity> getByExternalId(String walletId);

	int deleteByExternalId(String externalId);
}
