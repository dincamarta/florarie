/** Enum pentru statusurile posibile ale unei comenzi
 * @author Dinca (Mateas) Marta
 * @version 05 Ianuarie 2026
 */
package com.florarie.florarie.model;

public enum OrderStatus {
    NEW,              // noua (plasata de user)
    DELIVERED,         // preluata de admin
    IN_PROGRESS,   // in lucru
    READY,             // gata pentru ridicare/livrare
    CANCELED
}
