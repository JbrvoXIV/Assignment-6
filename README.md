### ADDED CHANGES

    if (sourceRow == rowsMinus1) {
        if(source.deadend) { // check if row 5, columns 0-3 contains DE
            source.nextStep = null;
        } else if(source.noDown) { // check if row 5, columns 0-3 contain XD
            if(source.horizontalJump > 0) {
                source.shortest = map[sourceRow][sourceCol + source.horizontalJump].shortest + source.wait + 1;
                source.nextStep = map[sourceRow][sourceCol + source.horizontalJump];
            } else {
                source.shortest = map[sourceRow][sourceCol + 1].shortest + source.wait + 1;
                source.nextStep = map[sourceRow][sourceCol + 1];
            }
        } else { // assign row 5, columns 0-4 the next piece with time depending on jump and wait time
            if(source.horizontalJump > 0) {
                source.shortest = map[sourceRow + source.verticalJump][sourceCol + source.horizontalJump].shortest + source.wait + 1;
                source.nextStep = map[sourceRow + source.verticalJump][sourceCol + source.horizontalJump];
            } else {
                source.shortest = map[sourceRow + source.verticalJump][sourceCol + 1].shortest + source.wait + 1;
                source.nextStep = map[sourceRow + source.verticalJump][sourceCol + 1];
            }
        }
    } else if (sourceCol == columnMinus1) {
        if(source.deadend) { // check if rows 0-5, column 4 contains DE
            source.nextStep = null;
        } else {
            if(source.noRight) { // check if rows 0-5, column 4 contains XR
                if(source.verticalJump > 0) {
                    source.shortest = map[sourceRow + source.verticalJump][sourceCol].shortest + source.wait + 1;
                    source.nextStep = map[sourceRow + source.verticalJump][sourceCol];
                } else {
                    source.shortest = map[sourceRow + 1][sourceCol].shortest + source.wait + 1;
                    source.nextStep = map[sourceRow + 1][sourceCol];
                }
            } else { // assign row 0-5, columns 4 the next piece with time depending on jump and wait time
                if(source.verticalJump > 0) {
                    source.shortest = map[sourceRow + source.verticalJump][sourceCol + source.horizontalJump].shortest + source.wait + 1;
                    source.nextStep = map[sourceRow + source.verticalJump][sourceCol + source.horizontalJump];
                } else {
                    source.shortest = map[sourceRow + 1][sourceCol + source.horizontalJump].shortest + source.wait + 1;
                    source.nextStep = map[sourceRow + 1][sourceCol + source.horizontalJump];
                }
            }
        }
    } else { // assign diagonal, right or bottom piece with appropriate jump and wait time for rows 0-4, columns 0-3
        Cell rightNeighbor = map[sourceRow][sourceCol + (source.horizontalJump > 0 ? source.horizontalJump : 1)],
        bottomNeighbor = map[sourceRow + (source.verticalJump > 0 ? source.verticalJump : 1)][sourceCol],
        diagonalNeighbor = (source.verticalJump > 0 && source.horizontalJump > 0) ? map[sourceRow + source.verticalJump][sourceCol + source.horizontalJump] : null;

        if(source.noRight) { // XR located
            rightNeighbor = null;
            if(diagonalNeighbor != null) { // diagonal ladder present
                source.shortest = Math.min(bottomNeighbor.shortest + source.wait, diagonalNeighbor.shortest + source.wait) + 1;
                if(source.shortest == bottomNeighbor.shortest + source.wait + 1) { // assign bottom neighbour piece
                    source.nextStep = bottomNeighbor;
                } else { // assign diagonal jump piece 
                    source.nextStep = diagonalNeighbor.nextStep; 
                }
            } else { // no diagonal ladder present, bottom piece if only possibility
                source.shortest = bottomNeighbor.shortest + source.wait + 1;
                source.nextStep = bottomNeighbor.nextStep;
            }
        } else if(source.noDown) { // XD located
            bottomNeighbor = null;
            if(diagonalNeighbor != null) { // diagonal ladder present
                source.shortest = Math.min(rightNeighbor.shortest + source.wait, diagonalNeighbor.shortest + source.wait) + 1;
                if(source.shortest == rightNeighbor.shortest + source.wait + 1) { // assign right neighbour piece
                    source.nextStep = rightNeighbor;
                } else { // assign diagonal jump piece
                    source.nextStep = diagonalNeighbor.nextStep;
                }
            } else { // no diagonal ladder present, right piece only possibility
                source.shortest = rightNeighbor.shortest + source.wait + 1;
                source.nextStep = rightNeighbor.nextStep;
            }
        } else { // No XR or XD located
            source.shortest = Math.min(bottomNeighbor.shortest + source.wait, rightNeighbor.shortest + source.wait) + 1;
            if(diagonalNeighbor != null) { // diagonal ladder located
                source.shortest = Math.min(source.shortest, diagonalNeighbor.shortest + source.wait + 1);
                if(source.shortest == bottomNeighbor.shortest + source.wait + 1) { // assign bottom piece
                    source.nextStep = bottomNeighbor;
                } else if(source.shortest == rightNeighbor.shortest + source.wait + 1) { // assign right piece
                    source.nextStep = rightNeighbor;
                } else { // assign diagonal piece
                    source.nextStep = diagonalNeighbor;
                }
            } else { // no diagonal ladder located
                if (source.shortest == bottomNeighbor.shortest + source.wait + 1) { // assign bottom piece
                    source.nextStep = bottomNeighbor;
                } else { // assign right piece
                    source.nextStep = rightNeighbor;
                }
            }
        }
    }

The above code is implemented inside of the Maze.java file under the solverHelper method. It is located from lines 88 - 180.