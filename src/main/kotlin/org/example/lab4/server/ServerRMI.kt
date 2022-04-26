package org.example.lab4.server

import java.rmi.registry.LocateRegistry
import java.rmi.registry.Registry
import java.rmi.server.UnicastRemoteObject

fun main() {
    if (System.getSecurityManager() == null) {
        System.setSecurityManager(SecurityManager())

    }
    System.setProperty("java.rmi.server.hostname","127.0.0.1")
    val solver: MatrixEquationSolver = MatrixEquationSolverImpl()

    try {
        val stub: MatrixEquationSolver = UnicastRemoteObject.exportObject(solver,0) as MatrixEquationSolver
        val registry: Registry = LocateRegistry.createRegistry(1011)
        registry.bind("MatrixEquationSolver", stub)
    } catch (e: Throwable) {
        println(e.message)
    }
}