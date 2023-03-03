# install.packages("cluster", repos = "http://cran.us.r-project.org")
# install.packages("factoextra", repos = "http://cran.us.r-project.org")
library(cluster)
library(factoextra)

chooseCRANmirror(graphics=FALSE, ind=1)

# Import the synthetic_control_data.txt file with regex \\s+ as the delimiter
mydata <- read.table("synthetic_control_data.txt", sep = "", header = TRUE)

# Convert the numeric vector to a data frame with a single column
df <- as.data.frame(mydata)

# View the structure of the data frame
str(df)

# Run k-means clustering with k=6
kmeans <- kmeans(df, centers = 6)

# Visualize the clusters using fviz_cluster and make it large enough to see
fviz_cluster(kmeans, data = df, ellipse.type = "convex", geom = "point", 
             palette = "jco", ggtheme = theme_minimal())