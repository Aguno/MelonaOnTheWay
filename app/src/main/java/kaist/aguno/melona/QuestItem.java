package kaist.aguno.melona;


public class QuestItem {
    //private란 변경이 되면 안 되는 변수에 대해 접근 제한 자 설정
    private String titleStr ;
    private String detailStr ;
    private String rewardStr;
    private String[] mData = new String [] {titleStr, detailStr, rewardStr};


    //파라메타값이 배열로된것을 사용함
    public QuestItem() {
        titleStr = "first";
        detailStr = "this is for detail";
        rewardStr = "100";
        mData = new String [] {titleStr, detailStr, rewardStr};
    }



    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setDetail(String desc) {
        detailStr = desc ;
    }
    public void setReward(String succ){rewardStr=succ;}

    public String getTitle() {
        return this.titleStr ;
    }
    public String getDetail() {
        return this.detailStr ;
    }
    public String getReward()  {
        return this.rewardStr ;
    }
    public String[] getData() {
        return mData;
    }
    public String getData(int index) {
        //이 구문은 getData(index) 값을 줬을 때 값을 불러올 mData에 값이 있는지 혹은 index 값이 mData가 가지고 있는 값을 초과했는지를 판별해주는 조건문
        if (mData == null || index >= mData.length) {
            return null;
        }
        return mData[index];
    }
}


