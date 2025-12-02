package com.escolinha.service;

import com.escolinha.domain.Fatura;

public interface PagamentoObserver {
    void onPagamentoRegistrado(Fatura fatura);
}