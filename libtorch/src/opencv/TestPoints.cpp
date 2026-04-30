#include "opencv2/opencv.hpp"

int main() {
    auto image { cv::imread("D:/tmp/lena.png") };
    cv::Mat dst;
    cv::drawKeypoints(image, { cv::KeyPoint(10, 10, 10), cv::KeyPoint(100, 100, 10) }, dst, cv::Scalar(0, 0, 0));
    cv::imshow("dst",   dst);
    cv::imshow("image", image);
    cv::waitKey();
    cv::destroyAllWindows();
    // 角点检测
    cv::cornerHarris(cv::imread("D:/tmp/lena.png", cv::IMREAD_GRAYSCALE), dst, 2, 3, 0.04);
    cv::normalize(dst, dst, 0, 255, cv::NORM_MINMAX);
    cv::convertScaleAbs(dst, dst);
    for(int row = 0; row < dst.rows; ++row) {
        for(int col = 0; col < dst.cols; ++col) {
            // if(dst.at<uchar>(row, col) < 100) {
            if(dst.at<uchar>(row, col) > 127) {
                cv::circle(image, cv::Point(col, row), 2, cv::Scalar(255, 255, 0));
            }
        }
    }
    cv::imshow("dst",   dst);
    cv::imshow("image", image);
    cv::waitKey();
    // 特征点检测
    // SIFT
    // SURF
    // ORB
    cv::destroyAllWindows();
    image = cv::imread("D:/tmp/lena.png");
    auto orb = cv::ORB::create();
    std::vector<cv::KeyPoint> points;
    orb->detect(image, points); // 检测关键点
    cv::Mat descriptions;
    orb->compute(image, points, descriptions); // 描述关键点
    auto imageAngel = image.clone();
    cv::drawKeypoints(image, points, image,      cv::Scalar(255, 255, 0));
    cv::drawKeypoints(image, points, imageAngel, cv::Scalar(255, 255, 0), cv::DrawMatchesFlags::DRAW_RICH_KEYPOINTS);
    cv::imshow("image",     image);
    cv::imshow("imageAngel", imageAngel);
    cv::waitKey();
    // 特征点匹配
    // BF匹配器
    // FLANN匹配器
    // RANSAC匹配
    cv::destroyAllWindows();
    auto yang1 { cv::imread("D:/tmp/yang1.jpg") };
    auto yang2 { cv::imread("D:/tmp/yang2.jpg") };
    cv::imshow("yang1", yang1);
    cv::imshow("yang2", yang2);
    std::vector<cv::KeyPoint> key_points1, key_points2;
    cv::Mat descriptions1, descriptions2;
    orb->detect(yang1, key_points1);
    orb->detect(yang2, key_points2);
    orb->compute(yang1, key_points1, descriptions1);
    orb->compute(yang2, key_points2, descriptions2);
    std::vector<cv::DMatch> matches, good_min, good_ransac;
    cv::BFMatcher matcher(cv::NORM_HAMMING);
    matcher.match(descriptions1, descriptions2, matches);
    double min_dist = 10000, max_dist = 0;
    for(int i = 0; i < matches.size(); ++i) {
        double dist = matches[i].distance;
        if(dist < min_dist) {
            min_dist = dist;
        }
        if(dist > max_dist) {
            max_dist = dist;
        }
    }
    for(int i = 0; i < matches.size(); ++i) {
        if(matches[i].distance <= std::max(2 * min_dist, 20.0)) {
        // if(matches[i].distance <= std::max(2 * min_dist, max_dist)) {
            good_min.push_back(matches[i]);
        }
    }
    std::vector<cv::Point2f> srcPoints(matches.size()), dstPoints(matches.size());
    for(int i = 0; i < matches.size(); ++i) {
        srcPoints[i] = key_points1[matches[i].queryIdx].pt;
        dstPoints[i] = key_points2[matches[i].trainIdx].pt;
    }
    std::vector<int> inliersMask(srcPoints.size());
    cv::findHomography(srcPoints, dstPoints, cv::RANSAC, 5, inliersMask);
    for(int i = 0; i < inliersMask.size(); ++i) {
        if(inliersMask[i]) {
            good_ransac.push_back(matches[i]);
        }
    }
    cv::Mat matches_out, good_min_out, good_ransac_out;
    cv::drawMatches(yang1, key_points1, yang2, key_points2, matches,     matches_out);
    cv::drawMatches(yang1, key_points1, yang2, key_points2, good_min,    good_min_out);
    cv::drawMatches(yang1, key_points1, yang2, key_points2, good_ransac, good_ransac_out);
    cv::imshow("matches_out",     matches_out);
    cv::imshow("good_min_out",    good_min_out);
    cv::imshow("good_ransac_out", good_ransac_out);
    cv::waitKey();
    return 0;
}