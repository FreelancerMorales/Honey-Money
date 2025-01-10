package com.honeymoney.Honey_Money.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.honeymoney.Honey_Money.model.MovimientoDiario;
import com.honeymoney.Honey_Money.model.MovimientosFinancieros;
import com.honeymoney.Honey_Money.model.TipoMovimiento;
import com.honeymoney.Honey_Money.repository.MovimientoDiarioRepository;
import com.honeymoney.Honey_Money.repository.MovimientosFinancierosRepository;
import com.honeymoney.Honey_Money.repository.TipoMovimientoRepository;
import com.honeymoney.Honey_Money.repository.UsuarioRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MovimientoDiarioService {
    @Autowired
    private MovimientoDiarioRepository movimientoDiarioRepository;
    
    @Autowired
    private MovimientosFinancierosRepository movimientosRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TipoMovimientoRepository tipoMovimientoRepository;

    @Transactional(readOnly = false)
    public void actualizarTotalesDiarios(Long usuarioId, LocalDate fecha) {
        if (usuarioId == null || fecha == null) {
            throw new IllegalArgumentException("Usuario ID y fecha son requeridos");
        }

        // Cargar y cachear TipoMovimiento antes de procesar
        Map<Long, TipoMovimiento> tipoMovimientoCache = new HashMap<>();
        tipoMovimientoRepository.findAll().forEach(tipo -> 
            tipoMovimientoCache.put(tipo.getId(), tipo));

        MovimientoDiario movimientoDiario = movimientoDiarioRepository
            .findByFechaAndUsuarioIdAndCierreDefinitivoFalse(fecha, usuarioId)
            .orElseGet(() -> crearNuevoMovimientoDiario(fecha, usuarioId));

        List<MovimientosFinancieros> movimientosDia = cargarMovimientosConTipoMovimiento(usuarioId, fecha, tipoMovimientoCache);
        
        actualizarTotales(movimientoDiario, movimientosDia);
        movimientoDiarioRepository.save(movimientoDiario);
    }

    private List<MovimientosFinancieros> cargarMovimientosConTipoMovimiento(
            Long usuarioId, 
            LocalDate fecha, 
            Map<Long, TipoMovimiento> tipoMovimientoCache) {
        
        return movimientosRepository.findByUsuarioIdAndFecha(usuarioId, fecha)
            .stream()
            .map(mov -> {
                if (mov.getTipoMovimiento() != null) {
                    Long tipoId = mov.getTipoMovimiento().getId();
                    TipoMovimiento tipoFromCache = tipoMovimientoCache.get(tipoId);
                    if (tipoFromCache != null) {
                        mov.setTipoMovimiento(tipoFromCache);
                    }
                }
                return mov;
            })
            .collect(Collectors.toList());
    }

    private MovimientoDiario crearNuevoMovimientoDiario(LocalDate fecha, Long usuarioId) {
        if (fecha == null || usuarioId == null) {
            throw new IllegalArgumentException("Fecha y usuarioId son requeridos");
        }

        MovimientoDiario nuevo = new MovimientoDiario();
        
        // Establecer datos básicos
        nuevo.setFecha(fecha);
        nuevo.setUsuario(usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + usuarioId)));

        // Asignar TipoMovimiento por defecto (usando ID 2 para gastos)
        TipoMovimiento tipoMovimiento = tipoMovimientoRepository.findById(2L)
            .orElseThrow(() -> new IllegalStateException("TipoMovimiento por defecto no encontrado"));
        nuevo.setTipoMovimiento(tipoMovimiento);

        // Validar estado antes de guardar
        if (nuevo.getFecha() == null) {
            throw new IllegalStateException("Fecha no puede ser null después de set");
        }
        if (nuevo.getUsuario() == null) {
            throw new IllegalStateException("Usuario no puede ser null después de set");
        }
        if (nuevo.getTipoMovimiento() == null) {
            throw new IllegalStateException("TipoMovimiento no puede ser null");
        }

        System.out.println("Creando MovimientoDiario con TipoMovimiento: " + nuevo.getTipoMovimiento().getId());

        // Guardar y verificar
        MovimientoDiario saved = movimientoDiarioRepository.save(nuevo);
        if (saved == null || saved.getId() == null) {
            throw new IllegalStateException("Error al guardar MovimientoDiario");
        }

        return saved;
    }

    private void actualizarTotales(MovimientoDiario movimientoDiario, 
                                    List<MovimientosFinancieros> movimientos) {
        // Validación inicial
        if (movimientoDiario == null || movimientos == null) {
            throw new IllegalArgumentException("MovimientoDiario y movimientos son requeridos");
        }

        // Hacer una copia de seguridad de los movimientos
        List<MovimientosFinancieros> movimientosCopy = new ArrayList<>(movimientos);
        
        BigDecimal totalIngresos = movimientosCopy.stream()
            .filter(m -> {
                TipoMovimiento tipo = m.getTipoMovimiento();
                return tipo != null && tipo.getId() == 1L;
            })
            .map(MovimientosFinancieros::getMonto)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        BigDecimal totalGastos = movimientosCopy.stream()
            .filter(m -> {
                TipoMovimiento tipo = m.getTipoMovimiento();
                return tipo != null && tipo.getId() == 2L;
            })
            .map(MovimientosFinancieros::getMonto)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (movimientoDiario == null || movimientos == null) {
            throw new IllegalArgumentException("MovimientoDiario y movimientos son requeridos");
        }

        // Asegurar que los TipoMovimiento estén cargados antes de procesar
        movimientos.forEach(m -> {
            if (m.getTipoMovimiento() == null) {
                throw new IllegalStateException("TipoMovimiento no puede ser null para el movimiento ID: " + m.getId());
            }
            // Forzar inicialización
            m.getTipoMovimiento().getId();
        });

        System.out.println("\n=== INICIO actualizarTotales ===");
        System.out.println("MovimientoDiario ID: " + movimientoDiario.getId());
        System.out.println("Cantidad de movimientos: " + movimientos.size());

        // Verificar estado de cada movimiento antes de procesar
        System.out.println("\n=== Estado de movimientos antes de procesar ===");
        movimientos.forEach(m -> {
            System.out.println("\nVerificando movimiento ID: " + m.getId());
            System.out.println("Objeto movimiento: " + m);
            System.out.println("TipoMovimiento reference: " + System.identityHashCode(m.getTipoMovimiento()));
            if (m.getTipoMovimiento() != null) {
                System.out.println("TipoMovimiento ID: " + m.getTipoMovimiento().getId());
            } else {
                System.out.println("WARN: TipoMovimiento es NULL");
            }
        });
        
        System.out.println();
        System.out.println("=== Calculando Ingresos ===");
        totalIngresos = movimientos.stream()
            .filter(m -> {
                boolean isIngreso = m.getTipoMovimiento() != null && m.getTipoMovimiento().getId() == 1L;
                System.out.println("Movimiento ID: " + m.getId() + 
                                 " - TipoMovimiento: " + (m.getTipoMovimiento() != null ? 
                                 m.getTipoMovimiento().getId() : "NULL") +
                                 " - Es ingreso: " + isIngreso);
                return isIngreso;
            })
            .map(MovimientosFinancieros::getMonto)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Verificar estado después de calcular ingresos
        System.out.println("\n=== Estado después de calcular ingresos ===");
        movimientos.forEach(m -> {
            System.out.println("\nVerificando movimiento ID: " + m.getId());
            System.out.println("TipoMovimiento reference: " + System.identityHashCode(m.getTipoMovimiento()));
            if (m.getTipoMovimiento() != null) {
                System.out.println("TipoMovimiento ID: " + m.getTipoMovimiento().getId());
            } else {
                System.out.println("WARN: TipoMovimiento es NULL");
            }
        });

        System.out.println();
        System.out.println("=== Calculando Gastos ===");
        totalGastos = movimientos.stream()
            .filter(m -> {
                boolean isGasto = m.getTipoMovimiento() != null && m.getTipoMovimiento().getId() == 2L;
                System.out.println("Movimiento ID: " + m.getId() + 
                                 " - TipoMovimiento: " + (m.getTipoMovimiento() != null ? 
                                 m.getTipoMovimiento().getId() : "NULL") +
                                 " - Es gasto: " + isGasto);
                return isGasto;
            })
            .map(MovimientosFinancieros::getMonto)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        System.out.println();
        System.out.println("Total Ingresos: " + totalIngresos);
        System.out.println("Total Gastos: " + totalGastos);
        
        movimientoDiario.setTotalIngresos(totalIngresos);
        movimientoDiario.setTotalGastos(totalGastos);
        movimientoDiario.setSaldoFinal(totalIngresos.subtract(totalGastos));
        movimientoDiario.setUltimaActualizacion(LocalDateTime.now());

        // Verificar estado final
        System.out.println("\n=== Estado final de movimientos ===");
        movimientos.forEach(m -> {
            System.out.println("\nVerificando movimiento ID: " + m.getId());
            System.out.println("TipoMovimiento reference: " + System.identityHashCode(m.getTipoMovimiento()));
            if (m.getTipoMovimiento() != null) {
                System.out.println("TipoMovimiento ID: " + m.getTipoMovimiento().getId());
            } else {
                System.out.println("WARN: TipoMovimiento es NULL");
            }
        });

        System.out.println("\n=== Estado Final de TipoMovimiento en Movimientos ===");
        movimientos.forEach(m -> {
            System.out.println("\nMovimiento ID: " + m.getId());
            System.out.println("TipoMovimiento final: " + 
                (m.getTipoMovimiento() != null ? 
                "ID: " + m.getTipoMovimiento().getId() + 
                ", Nombre: " + m.getTipoMovimiento().getNombre() : 
                "NULL"));
        });
        
        System.out.println("=== FIN actualizarTotales ===");
        System.out.println();
    }
}