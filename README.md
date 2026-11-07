# Parcial 3 - Programación I: Panadería El Trigal

Sistema en Java 21 + JavaFX que registra pedidos de pan, los asigna al panadero que tarda menos según su forma de trabajo y recupera el tiempo reservado cuando un pedido se cancela.

## Estructura (arquitectura vista en clase)

```
src/main/java/co/edu/uniquindio/poo/panaderia
├── model            -> lógica de negocio (Persona, Empleado, Panadero, PanaderoArtesanal,
│                       PanaderoIndustrial, Cajero, Pedido, Panaderia, IProduccion,
│                       TipoPan, EstadoPedido)
├── controller       -> EmpleadoController, PedidoController (puente vista - modelo)
├── viewcontrollers  -> InicioViewController, EmpleadoViewController, PedidoViewController
└── app              -> Aplicacion (punto de entrada JavaFX)
src/main/resources/co/edu/uniquindio/poo/panaderia -> inicio.fxml, empleados.fxml, pedidos.fxml (view)
src/test/java/...    -> pruebas JUnit 5
```

## Reglas de producción

| Empleado | Forma de trabajo | Tradicional | Integral |
|---|---|---|---|
| Panadero artesanal | Bandejas de hasta 12 panes | 30 min por bandeja | 40 min por bandeja |
| Panadero industrial | 20 min de preparación por pedido + lotes de hasta 30 | 25 min por lote | 35 min por lote |

Una bandeja o lote incompleto cuenta como uno completo (división redondeada hacia arriba).

## Ejecutar

```bash
mvn clean test        # 29 pruebas (9 obligatorias en PanaderiaTest)
mvn clean javafx:run  # abre la interfaz gráfica
```
