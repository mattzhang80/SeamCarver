Programming Assignment 7: Seam Carving


/* *****************************************************************************
 *  Describe concisely your algorithm to find a horizontal (or vertical)
 *  seam.
 **************************************************************************** */
We used Dijkstra's algorithm to find the shortest path (seam). Each element in
the top row is a source. Then, it goes through each adjacent element on the row
beneath it and updates the necessary things like distTo, edgeTo, etc.
To find the actual shortest path, it starts from the bottom up. For the other
orientation we just transposed the image, ran the method, and then transposed
it back.


/* *****************************************************************************
 *  Describe what makes an image suitable to the seam-carving approach
 *  (in terms of preserving the content and structure of the original
 *  image, without introducing visual artifacts). Describe an image that
 *  would not work well.
 **************************************************************************** */
Images that work best are ones that have heavy content (e.g. person/building) in
distinct areas that are one chunk as this gives them higher energy contrast with
surroundings, meaning that areas in and around it will most likely not be
removed. As all the other stuff in the photo is likely background or stuff with
continous chunks (e.g. landscape), seamcarving can most likely find a path that
ignores the heavy content and picks background stuff.

 images that won't work quite well are images that share very similar colors
 throughout (i.e not having clear color boundaries). An example would be if the
 lighting was terrible and a subject (person or building kind of blends in and
 thus would have low energy and would be removed, despite it being a necessasry
 part.

/* *****************************************************************************
 *  Perform computational experiments to estimate the running time to reduce
 *  a W-by-H image by one column and one row (i.e., one call each to
 *  findVerticalSeam(), removeVerticalSeam(), findHorizontalSeam(), and
 *  removeHorizontalSeam()). Use a "doubling" hypothesis, where you
 *  successively increase either W or H by a constant multiplicative
 *  factor (not necessarily 2).
 *
 *  To do so, fill in the two tables below. Each table must have 5-10
 *  data points, ranging in time from around 0.25 seconds for the smallest
 *  data point to around 30 seconds for the largest one.
 **************************************************************************** */

(keep W constant)
 W = 2000
 multiplicative factor (for H) = 2

 H           time (seconds)      ratio       log ratio
------------------------------------------------------
200          .351                N/A         N/A
400          .589                1.67        0.746
800          1.183               2.00        1.006
1600         2.407               2.03        1.024
3200         5.142               2.136       1.095
6400         11.387              2.21        1.146
12,800       25.777              2.26        1.179


(keep H constant)
 H = 2000
 multiplicative factor (for W) = 2

 W           time (seconds)      ratio       log ratio
------------------------------------------------------
200          .346                N/A         N/A
400          .488               1.41         0.496
800          1.04               2.13         1.092
1600         2.33               2.35         1.164
3200         5.37               2.30         1.205
6400         11.73              2.18         1.127
12,800       26.98              2.30         1.202



/* *****************************************************************************
 *  Using the empirical data from the above two tables, give a formula
 *  (using tilde notation) for the running time (in seconds) as a function
 *  of both W and H, such as
 *
 *       ~ 5.3*10^-8 * W^5.1 * H^1.5
 *
 *  Briefly explain how you determined the formula for the running time.
 *  Recall that with tilde notation, you include both the coefficient
 *  and exponents of the leading term (but not lower-order terms).
 *  Round each coefficient and exponent to two significant digits.
 **************************************************************************** */


Running time (in seconds) to find and remove one horizontal seam and one
vertical seam, as a function of both W and H:


    ~3.7*10^-9 * W^1.2 * H^1.2
We used the formula T(n) = a * W^w * H^h. We found W and H by doing
log2(lastPoint/prevPoint) for each of them using their respective data points.
After that, we picked the same Time for both of them using consistent W and H
for both. We then plugged into the general equation and solved for a.



/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */




/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */

It took a super long time figuring out how to adapt Dijkstra to a 2D array.
It also took us a long time figuring out why the transpose loop would not
terminate and that's because we used picture().setRGB rather than picture.setRGB.
Trying to optize was also a pain because we couldn't figure out how to use the
energy[][] array effectively.


/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
we actually really understand how Dijsktra's works now. Still fuzzy on the other
algos tho, ngl.
