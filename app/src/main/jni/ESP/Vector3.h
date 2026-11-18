#pragma once
#include <cmath>

struct Vector2 {
    float x, y;
};

struct Vector3 {
    float x, y, z;
    Vector3() : x(0), y(0), z(0) {}
    Vector3(float x, float y, float z) : x(x), y(y), z(z) {}
};

inline bool WorldToScreen(Vector3 worldPos, Vector2& screenPos, float* viewMatrix, int screenWidth, int screenHeight) {
    float w = viewMatrix[3] * worldPos.x + viewMatrix[7] * worldPos.y + viewMatrix[11] * worldPos.z + viewMatrix[15];
    if (w < 0.01f) return false;
    float invW = 1.0f / w;
    float x = (viewMatrix[0] * worldPos.x + viewMatrix[4] * worldPos.y + viewMatrix[8] * worldPos.z + viewMatrix[12]) * invW;
    float y = (viewMatrix[1] * worldPos.x + viewMatrix[5] * worldPos.y + viewMatrix[9] * worldPos.z + viewMatrix[13]) * invW;
    screenPos.x = (screenWidth / 2.0f) + (x * screenWidth / 2.0f);
    screenPos.y = (screenHeight / 2.0f) - (y * screenHeight / 2.0f);
    return true;
}
