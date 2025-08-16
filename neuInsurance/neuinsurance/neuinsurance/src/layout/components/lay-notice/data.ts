export interface ListItem {
  avatar: string;
  title: string;
  datetime: string;
  type: string;
  description: string;
  status?: "primary" | "success" | "warning" | "info" | "danger";
  extra?: string;
}

export interface TabItem {
  key: string;
  name: string;
  list: ListItem[];
  emptyText: string;
}

export const noticesData: TabItem[] = [
  {
    key: "1",
    name: "通知",
    list: [],
    emptyText: "暂无通知"
  },
  {
    key: "2",
    name: "消息",
    list: [
      {
        avatar: "https://xiaoxian521.github.io/hyperlink/svg/smile1.svg",
        title: "今日份工作",
        description: "今日份工作完成情况：完成了前后端联调，修复了部分bug，",
        datetime: "今天",
        type: "2"
      },
      {
        avatar: "https://xiaoxian521.github.io/hyperlink/svg/smile2.svg",
        title: "昨日工作",
        description: "进行了后端的接口测试，修复了部分bug，完成了前端的样式设计",
        datetime: "昨天",
        type: "2"
      },
    ],
    emptyText: "暂无消息"
  },
  {
    key: "3",
    name: "待办",
    list: [
      {
        avatar: "",
        title: "第三方紧急代码变更",
        description:
          "小林提交于 2025-07-15，需在 2025-07-16 前完成代码变更任务",
        datetime: "",
        extra: "马上到期",
        status: "danger",
        type: "3"
      },
      {
        avatar: "",
        title: "版本发布",
        description: "指派小铭于 2025-07-16 前完成更新并发布",
        datetime: "",
        extra: "已耗时 8 天",
        status: "warning",
        type: "3"
      },
      {
        avatar: "",
        title: "新功能开发",
        description: "开发多租户管理",
        datetime: "",
        extra: "进行中",
        type: "3"
      },
      {
        avatar: "",
        title: "任务名称",
        description: "任务需要在 2025-07-16 10:00 前启动",
        datetime: "",
        extra: "未开始",
        status: "info",
        type: "3"
      }
    ],
    emptyText: "暂无待办"
  }
];
