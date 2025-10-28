package com.escolinha.service;

import com.escolinha.domain.Comunicado;
import com.escolinha.repository.ComunicadoRepository;
import java.time.LocalDateTime;
 
 public class ComunicadoService {
     private final ComunicadoRepository repo;
     public ComunicadoService(ComunicadoRepository r) { this.repo = r; }
     public Comunicado publicarComunicadoGeral(int autorId, String titulo, String msg) {
         Comunicado c = new Comunicado(0, titulo, msg, LocalDateTime.now(), autorId, null); // Turma null = Geral
         return repo.salvar(c);
     }
}