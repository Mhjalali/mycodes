#include <stdio.h>
#include "stdlib.h"
//node
struct node {
    struct node *parent;
    struct node *left;
    struct node *right;
    int key;
    int depth;
};
//for easy notation
typedef struct node *Node;
//the depth of the whole tree
int total_depth;
//insert nodes
void insert(Node *self, Node parent, int key, int depth) {
    int space = sizeof(struct node);
    if (*self == NULL) {
        *self = malloc(space);
        if (*self != NULL) {
            (*self)->parent = parent;
            (*self)->right = NULL;
            (*self)->left = NULL;
            (*self)->key = key;
            (*self)->depth = depth;
            total_depth = (total_depth > depth) ? total_depth : depth;
        }
    } else {

        if (key < (*self)->key) {
            insert(&((*self)->left), *self, key, ((*self)->depth) + 1);
        }
        if (key > (*self)->key) {
            insert(&((*self)->right), *self, key, ((*self)->depth) + 1);
        }
    }
}
//find the nearest proper right
Node right_finder(Node currentnode) {
    if (currentnode->parent == NULL)
        return NULL;

    if (currentnode->parent->right == NULL) {
        right_finder(currentnode->parent);
    } else {
        if (currentnode->parent->right == currentnode)
            right_finder(currentnode->parent);
        else
            return currentnode->parent->right;
    }
}
//find sum in every time
void grapes(Node currentnode, int depth_reached, int *sum) {
    int new_depth_reached = depth_reached;
    if (currentnode->depth > depth_reached) {
        (*sum) += currentnode->key;
        new_depth_reached = depth_reached + 1;
    }
    if (currentnode->depth == total_depth)
        return;

    if (currentnode->left != NULL) {
        grapes(currentnode->left, new_depth_reached, sum);
    } else {
        if (currentnode->right != NULL) {
            grapes(currentnode->right, new_depth_reached, sum);
        } else {
            if (currentnode->parent == NULL)
                return;
            else{
                Node hold = right_finder(currentnode);
                grapes(hold, new_depth_reached, sum);
            }
        }
    }
}


main() {
    int n, q;
    scanf("%d %d", &n, &q);
    int nodes[n];
    for (int i = 0; i < n; ++i) {
        scanf("%d", &nodes[i]);
    }
    int new_nodes[q];
    for (int i = 0; i < q; ++i) {
        scanf("%d", &new_nodes[i]);
    }

    total_depth = 0;
    Node root = NULL;

    for (int i = 0; i < n; ++i) {
        insert(&root, NULL, nodes[i], 1);
    }

    int *sum;
    int totoal = 0;
    sum = &totoal;
    //first time
    grapes(root, 0, sum);

    printf("%d\n", totoal);
    for (int i = 0; i < q; ++i) {
        insert(&root, NULL, new_nodes[i], 1);
        totoal = 0;
        grapes(root, 0, sum);
        printf("%d\n", totoal);
    }
}

