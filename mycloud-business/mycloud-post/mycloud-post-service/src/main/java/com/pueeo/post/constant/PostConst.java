package com.pueeo.post.constant;

public class PostConst {

    /**
     * 帖子状态 0-审核中 1-审核通过 2-审核不通过 3-用户删除 4-违规删除
     */
    public static class PostStatus{
        public final static int auditing = 0;
        public final static int pass = 1;
        public final static int unpass = 2;
        public final static int user_delete = 3;
        public final static int violation = 4;
    }

    /**
     * 帖子类型 1-文字 2-图片 3-视频
     */
    public static class PostType{
        public final static int text = 1;
        public final static int image = 2;
        public final static int video = 3;
    }
}
