#include <stdio.h>

// for sorting
void merge(long long int array1[], int left, int middle, int right) {
    int i, j, k;
    long long int len_l = middle - left + 1;
    long long int len_r = right - middle;
    long long int flag;
    long long int L1[len_l], R1[len_r];

    for (i = 0; i < len_l; i++) {
        L1[i] = array1[left + i];
    }
    for (j = 0; j < len_r; j++) {
        R1[j] = array1[middle + 1 + j];
    }

    i = 0;
    j = 0;
    k = left;

    while (1 == 1) {
        if (L1[i] <= R1[j]) {
            array1[k] = L1[i];
            i++;
        } else {
            array1[k] = R1[j];
            j++;
        }
        k++;
        if (i == len_l) {
            flag = 0;
            break;
        } else if (j == len_r) {
            flag = 1;
            break;
        }
    }

    if (flag) {
        for (int l = i; l < len_l; ++l && k++) {
            array1[k] = L1[l];
        }
    } else {
        for (int l = j; l < len_r; ++l && k++) {
            array1[k] = R1[l];
        }
    }

}

void Sort(long long int array1[], int left, int right) {
    if (left < right) {
        int middle = (right + left) / 2;
        Sort(array1, left, middle);
        Sort(array1, middle + 1, right);
        merge(array1, left, middle, right);
    }
}

main() {
    //getting data
    long long int n, start;
    scanf("%lld %lld", &n, &start);
    long long int cities[5000][2] = {0};
    long long int points_p[10000] = {0};
    long long int points[10000] = {0};

    for (int i = 0; i < n; ++i) {
        long long int hold[2];
        scanf("%lld %lld", &hold[0], &hold[1]);
        cities[i][0] = hold[0];
        cities[i][1] = hold[1];
        points_p[2 * i] = hold[0];
        points_p[2 * i + 1] = hold[1];
    }

    //sort and remove repeated points
    Sort(points_p, 0, 2 * n - 1);
    int np = 0; //number of not repetitive points
    for (int i = 0; i < 2 * n; ++i) {
        if (points_p[i] != points_p[i - 1]) {
            points[np] = points_p[i];
            np++;
        }
    }

    //filling the dp matrix we cant have 5000 rows so we keep only 2 rows
    long long int dp[2][10000] = {0};
    for (int j = 0; j < np; ++j) {
        dp[0][j] = (start > points[j]) ? start - points[j] : points[j] - start;
        long long int cost = 0;
        if (points[j] > cities[0][1])
            cost = points[j] - cities[0][1];
        else if (points[j] < cities[0][0])
            cost = cities[0][0] - points[j];
        else
            cost = 0;
        dp[0][j] += cost;
    }
    for (int i = 1; i < n; ++i) {
        for (int j = 0; j < np; ++j) {
            if (j == 0)
                dp[1][0] = dp[0][0];
            else if (dp[0][j] > (dp[1][j - 1] + points[j] - points[j - 1]))
                dp[1][j] = dp[1][j - 1] + points[j] - points[j - 1];
            else
                dp[1][j] = dp[0][j];
        }
        for (int j = np - 2; j >= 0; --j) {
            if (dp[1][j] > (dp[1][j + 1] + points[j + 1] - points[j]))
                dp[1][j] = dp[1][j + 1] + points[j + 1] - points[j];
        }
        for (int j = 0; j < np; ++j) {
            long long int cost = 0;
            if (points[j] > cities[i][1])
                cost = points[j] - cities[i][1];
            else if (points[j] < cities[i][0])
                cost = cities[i][0] - points[j];
            else
                cost = 0;
            dp[1][j] += cost;
        }
        // setting the first row for next level
        for (int j = 0; j < np; ++j) {
            dp[0][j] = dp[1][j];
        }
    }
    //finding min
    long long int min = dp[1][0];
    for (int j = 1; j < np; ++j) {
        if (min > dp[1][j])
            min = dp[1][j];
    }
    printf("%lld", min);
}
