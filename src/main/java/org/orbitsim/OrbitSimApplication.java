package org.orbitsim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class OrbitSimApplication {
    private static final double R_EARTH = 6_371_000;
    private static final double MU = 3.986004418e14;

    private static double computeAngularVelocity(double r) {
        return Math.sqrt(MU/Math.pow(r,3));
    }

    private static double computeOrbitalPeriod(double r){
        double w = computeAngularVelocity(r);
        return 2*Math.PI / w;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter attitude in km: ");
        double attitudeKm = sc.nextDouble();
        System.out.println("Enter time step in seconds: ");
        double timeStepInSeconds = sc.nextDouble();
        System.out.println("Enter number of steps: ");
        int steps = sc.nextInt();

        double altitudeM = attitudeKm * 1000.0;
        double r = R_EARTH + altitudeM;
        double omega = computeAngularVelocity(r);
        double periodSec = computeOrbitalPeriod(r);
        double periodMinutes = periodSec / 60.0;

        System.out.printf("Orbital radius r = %.0f m%n", r);
        System.out.printf("Angular velocity ω = %.6e rad/s%n", omega);
        System.out.printf("Orbital period T = %.1f s (≈ %.2f min)%n", periodSec, periodMinutes);
        System.out.println("----- Simulation -----");
        for(int i=0; i < steps; i++){
            double t = i * timeStepInSeconds;
            double angleRad = omega * t;
            double x = r * Math.cos(angleRad);
            double y = r * Math.sin(angleRad);

            double radiusNow = Math.sqrt(x * x + y * y); // Pythagorean theory in 2D
            System.out.printf(
                    "t=%6.1f s | x=%12.2f m | y=%12.2f m | r=%.2f m%n",
                    t, x, y, radiusNow
            );
        }

    }

}

/*
Kepler's Laws
    First: Planets orbit in ellipses with the Sun at one focus
    Second: Planets sweep out equal areas in equal times (move faster when close to the sun and slower when far from it)
    Third: The square of the orbital period is proportional to the cube of the semi-major axis
    P^2 <=> a^3
*/


/*
Theory Math
    Earth Radius (R) = 6 371 000 meters = 6371 km
    Standard Earth Gravitational parameter (u) = 3.98 * 10^14 m^3/s^2
    Attitude (h) of satellite above earth (r) = R + h
    Orbital period (T) = 2*Pi / w = 2*Pi * sqrt(r^3/u)
    Circular orbit angular velocity (w) = sqrt(u/T^3)
    Position in 2D at time t: x(t) = r*cos(wt), y(t) = r*sin(wt)

*/
