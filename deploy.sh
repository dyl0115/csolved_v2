#!/bin/bash

set -e

# 설정
NGINX_CONF_TEMPLATE="./nginx/nginx.conf.template"
NGINX_CONF="./nginx/nginx.conf"
ACTIVE_APP="./nginx/active_app"
MAX_RETRY=30

# 현재 활성 앱 확인
get_current_app()
{
  if [ -f "$ACTIVE_APP" ]; then
    CURRENT_APP=$(cat "$ACTIVE_APP")
    echo "현재 활성: $CURRENT_APP"
  else
    CURRENT_APP=""
    echo "최초 배포 입니다."
  fi
}

# 다음에 띄울 앱 결정
decide_next_app()
{
  if [ "$CURRENT_APP" == "blue" ]; then
      NEXT_APP="green"
  else
      NEXT_APP="blue"
  fi
  echo "새로 띄울 앱: $NEXT_APP"
}

# 새 컨테이너 시작
start_new_app()
{
  echo "새 앱 시작: app-$NEXT_APP"
  docker compose up -d app-$NEXT_APP --build
}

# 헬스 체크 대기
wait_for_health()
{
  echo "헬스 체크 대기 중..."

  local RETRY_COUNT=0

  while [ $RETRY_COUNT -lt $MAX_RETRY ]; do
    local HEALTH=$(docker inspect --format='{{.State.Health.Status}}' csolved_v2-app-$NEXT_APP-1 2>/dev/null || echo "starting")

    if [ "$HEALTH" == "healthy" ]; then
        echo "헬스 체크 성공"
        return 0
    fi

    echo "대기 중... ($RETRY_COUNT/$MAX_RETRY)"
    sleep 5
    RETRY_COUNT=$((RETRY_COUNT + 1))
  done

  echo "헬스체크 실패!"
  return 1
}

# nginx 전환
switch_traffic()
{
  echo "트래픽 전환: app-$NEXT_APP"

  # nginx.conf 생성 (nginx.conf.template 속 변수를 치환함. envsubst 사용)
  export TARGET_APP="app-$NEXT_APP"
  envsubst '$TARGET_APP' < $NGINX_CONF_TEMPLATE > $NGINX_CONF

  # nginx 시작 (이미 있으면 무시됨) + 설정 reload
  docker compose up -d nginx
  docker compose exec nginx nginx -s reload

  # 상태 파일 업데이트
  echo "$NEXT_APP" > $ACTIVE_APP
}

stop_old_app()
{
  if [ -n "$CURRENT_APP" ]; then
    echo "이전 앱 중지: app-$CURRENT_APP"
    docker compose stop app-$CURRENT_APP
  fi

  # 사용하지 않는 이미지 정리
  echo "이미지 정리 중..."
  docker image prune -f
}

echo "========== 배포 시작 =========="

get_current_app
decide_next_app
start_new_app
wait_for_health
switch_traffic
stop_old_app

echo "========== 배포 완료 =========="
