package org.example.lab4.client

import org.example.lab4.server.MatrixEquationSolver
import java.rmi.registry.LocateRegistry
import java.rmi.registry.Registry

fun main() {
    if (System.getSecurityManager() == null) {
        System.setSecurityManager(SecurityManager())
    }
    val registry: Registry = LocateRegistry.getRegistry(1011)
    val stub: MatrixEquationSolver = registry.lookup("MatrixEquationSolver") as MatrixEquationSolver
    print(stub.solveForN(1000))
}