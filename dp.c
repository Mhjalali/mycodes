#include <stdio.h>

// for sorting arrays
void merge(int array1[], int left, int middle, int right) {
    int i, j, k;
    int len_l = middle - left + 1;
    int len_r = right - middle;
    int flag;
    int L1[len_l], R1[len_r];

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
        if (L1[i] >= R1[j]) {
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

void Sort(int array1[], int left, int right) {
    if (left < right) {
        int middle = (right + left) / 2;
        Sort(array1, left, middle);
        Sort(array1, middle + 1, right);
        merge(array1, left, middle, right);
    }
}

main() {
    int n = 0, m = 0, k = 0;
    scanf("%d %d %d", &n, &m, &k);
    int num[n][m];
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < m; ++j) {
            scanf("%d", &num[i][j]);
        }
    }
    // sort the array
    for (int i = 0; i < n; ++i) {
        Sort(num[i], 0, m - 1);
    }

    int hold[71][71] = {0}; //matrix that each time holds sum of the row

    for (int l = 0; l < n; ++l) {
        int x;
        int counter[71][71] = {0}; //matrix that holds how many numbers we used of the row

        for (int i = 1; i < m + 1; ++i) {
            for (int j = 0; j < k; ++j) {
                counter[i][j] = counter[i - 1][j];
            }
            for (int j = 0; j < k; ++j) {
                x = (j + k - num[l][i - 1] % k) % k;

                int previous_num; // the last number of x column that has an acceptable count
                int previous_count;
                int shift = 0; // the times we should climb of the x column to find somewhere with acceptable count
                do {
                    previous_num = hold[i - 1 - shift][x];
                    previous_count = counter[i - 1 - shift][x];
                    shift++;
                } while (counter[i - 1 - shift + 1][x] >= (m / 2)); //check the count of the numbers added

                if (x != 0 && previous_num == 0) {
                    hold[i][j] = hold[i - 1][j]; // hold the previous row
                } else if ((previous_num + num[l][i - 1]) > hold[i - 1][j]) {
                    hold[i][j] = previous_num + num[l][i - 1]; //add the new number
                    counter[i][j] = previous_count + 1; // update the counts
                } else {
                    hold[i][j] = hold[i - 1][j]; // hold the previous row
                }
            }
        }

        for (int i = 0; i < k; ++i) // transferring the last row to the first row of hold matrix for the next row of the main matrix
            hold[0][i] = hold[m][i];
        if (l != n - 1) // resetting the hold matrix except the first row
            for (int i = 1; i < m + 1; ++i) {
                for (int j = 0; j < k; ++j) {
                    hold[i][j] = 0;
                    counter[i][j] = 0;
                }
            }
    }

    printf("%d", hold[m][0]); // the answer is the first number of the last row of hold
}
