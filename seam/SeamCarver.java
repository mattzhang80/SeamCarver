import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {

    private Picture picture; // input pic

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException("invalid input");
        this.picture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || y < 0 || y >= picture.height() || x >= picture.width())
            throw new IllegalArgumentException("pixel outside range");

        int prevRow = picture.getRGB(x, (y + height() - 1) % height());
        int nextRow = picture.getRGB(x, (y + 1) % height());
        int prevCol = picture.getRGB((x + width() - 1) % width(), y);
        int nextCol = picture.getRGB((x + 1) % width(), y);

        double deltaX = getEnergy(prevCol, nextCol);
        double deltaY = getEnergy(prevRow, nextRow);

        return Math.sqrt(deltaX + deltaY);
    }

    // calculates the color diff from prev and next on resp. axis
    private double getEnergy(int prev, int next) {
        double r = ((prev >> 16) & 0xFF) - ((next >> 16) & 0xFF);
        double g = ((prev >> 8) & 0xFF) - ((next >> 8) & 0xFF);
        double b = ((prev & 0xFF)) - ((next & 0xFF));

        return r * r + g * g + b * b;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose();
        int[] seam = findVerticalSeam();
        transpose();
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] seam = new int[height()];
        double[][] distTo = new double[width()][height()];
        int[][] edgeTo = new int[width()][height()];
        double[][] energy = new double[width()][height()];
        int minIndex = -1;

        // inits all distances other than first row to infinity
        for (int i = 0; i < width(); i++) {
            double currentEnergy = energy(i, 0);
            distTo[i][0] = currentEnergy;
            energy[i][0] = currentEnergy;
        }
        for (int j = 1; j < height(); j++) {
            for (int i = 0; i < width(); i++) {
                distTo[i][j] = Double.POSITIVE_INFINITY;

                double currentEnergy = energy(i, j);
                energy[i][j] = currentEnergy;
            }
        }

        // inserts the initial points (top row)
        IndexMinPQ<Double> pq = new IndexMinPQ<Double>(height() * width());
        for (int i = 0; i < width(); i++) {
            pq.insert(i, distTo[i][0]);
        }

        // relaxes points
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            int row = v / width();
            int col = v % width();

            // checks three pixels below/left, below, and below/right
            for (int i = col - 1; i <= col + 1; i++) {
                if (i >= 0 && i < width() && row != height() - 1) {
                    int w = (row + 1) * width() + i;
                    if (distTo[i][row + 1] > distTo[col][row] + energy[i][row + 1]) {
                        distTo[i][row + 1] = distTo[col][row] + energy[i][row + 1];
                        edgeTo[i][row + 1] = col;
                        if (row == height() - 1) break;
                        if (pq.contains(w)) {
                            pq.decreaseKey(w, distTo[i][row + 1]);
                        }
                        else {
                            pq.insert(w, distTo[i][row + 1]);
                        }


                    }
                }
            }
            minIndex = col;
            if (row == height() - 1) break;
        }

        seam[height() - 1] = minIndex;


        for (int i = height() - 2; i >= 0; i--) {
            seam[i] = edgeTo[seam[i + 1]][i + 1];
        }

        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        transpose();
        removeVerticalSeam(seam);
        transpose();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("seam is null");
        if (seam.length > height() || seam.length < height())
            throw new IllegalArgumentException("wrong size");
        for (int i = 0; i < seam.length; i++) {
            int col = seam[i];
            if (col < 0 || col >= width()) {
                throw new IllegalArgumentException("bad seam");
            }
            if (i > 0 && Math.abs(col - seam[i - 1]) > 1) {
                throw new IllegalArgumentException("bad seam");
            }
        }

        Picture newPic = new Picture(width() - 1, height());
        for (int r = 0; r < height(); r++) {
            for (int c = 0; c < width() - 1; c++) {
                if (c < seam[r]) {
                    newPic.setRGB(c, r, picture.getRGB(c, r));
                }
                if (c >= seam[r]) {
                    newPic.setRGB(c, r, picture.getRGB(c + 1, r));
                }
            }
        }

        picture = newPic;
    }

    // transposes image by swapping vertical and horizontal
    private void transpose() {
        Picture transposed = new Picture(height(), width());
        for (int c = 0; c < width(); c++) {
            for (int r = 0; r < height(); r++) {
                transposed.setRGB(r, c, picture.getRGB(c, r));
            }
        }
        picture = transposed;
    }


    //  unit testing (required)
    public static void main(String[] args) {
        Picture pic = new Picture("6x5.png");
        SeamCarver test = new SeamCarver(pic);
        int[] vertSeam = test.findVerticalSeam();
        int[] horiSeam = test.findHorizontalSeam();
        int[] badSeam = new int[6];

        StdOut.println(test.height());
        StdOut.println(test.width());
        StdOut.println("Energy: " + test.energy(0, 0));
        for (int i = 0; i < vertSeam.length; i++) {
            StdOut.print(vertSeam[i] + " ");
        }

        StdOut.println();

        for (int i = 0; i < horiSeam.length; i++) {
            StdOut.print(horiSeam[i] + " ");
        }

        StdOut.println();

        test.removeVerticalSeam(vertSeam);
        test.removeHorizontalSeam(horiSeam);
        test.picture().show();
        test.removeVerticalSeam(badSeam);
    }
}
