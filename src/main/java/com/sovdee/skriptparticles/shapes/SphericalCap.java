package com.sovdee.skriptparticles.shapes;

import com.sovdee.skriptparticles.util.MathUtil;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class SphericalCap extends AbstractShape implements RadialShape, CutoffShape {

    private double radius;
    private double cutoffAngle;
    private double cutoffAngleCos;

    public SphericalCap(double radius, double cutoffAngle) {
        super();
        this.radius = radius;
        this.cutoffAngle = cutoffAngle;
        this.cutoffAngleCos = Math.cos(cutoffAngle);
    }

    @Override
    public Set<Vector> generateOutline() {
        return generateSurface();
    }

    @Override
    public Set<Vector> generateSurface() {
        int pointCount = 4 * (int) (Math.PI * radius * radius / (particleDensity * particleDensity));
        return MathUtil.calculateFibonacciSphere(pointCount, radius, cutoffAngle);
    }

    @Override
    public Set<Vector> generateFilled() {
        Set<Vector> points = new HashSet<>();
        for (double r = radius; r > 0; r -= particleDensity) {
            int pointCount = 4 * (int) (Math.PI * r * r / (particleDensity * particleDensity));
            points.addAll(MathUtil.calculateFibonacciSphere(pointCount, r, cutoffAngle));
        }
        return points;
    }


    @Override
    public void setParticleCount(int particleCount) {
        this.particleDensity =  switch (style) {
            case OUTLINE, SURFACE -> Math.sqrt(2 * Math.PI * radius * radius * (1 - cutoffAngleCos) / particleCount);
            case FILL -> Math.cbrt(Math.PI / 3 * radius * radius * radius * (2 + cutoffAngleCos) * (1 - cutoffAngleCos) * (1 - cutoffAngleCos) / particleCount);
        };
        needsUpdate = true;
    }

    @Override
    public double getCutoffAngle() {
        return cutoffAngle;
    }

    @Override
    public void setCutoffAngle(double cutoffAngle) {
        this.cutoffAngle = MathUtil.clamp(cutoffAngle, 0, Math.PI);
        this.cutoffAngleCos = Math.cos(this.cutoffAngle);
        needsUpdate = true;
    }

    @Override
    public double getRadius() {
        return radius;
    }

    @Override
    public void setRadius(double radius) {
        this.radius = Math.max(radius,0);
        needsUpdate = true;
    }

    @Override
    public Shape clone() {
        SphericalCap sphericalCap = new SphericalCap(radius, cutoffAngle);
        return this.copyTo(sphericalCap);
    }
}
