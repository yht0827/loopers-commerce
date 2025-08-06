package com.loopers.infrastructure.brand;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.brand.Brand;

public interface BrandJpaRepository extends JpaRepository<Brand, Long> {

}
