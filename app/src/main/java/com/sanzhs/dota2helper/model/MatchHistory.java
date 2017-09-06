package com.sanzhs.dota2helper.model;

import java.util.List;

/**
 * Created by sanzhs on 2017/9/6.
 */

public class MatchHistory {
    /**
     * result : {"status":1,"num_results":5,"total_results":500,"results_remaining":495,"matches":[{"match_id":3420117698,"match_seq_num":2981004766,"start_time":1504365180,"lobby_type":0,"radiant_team_id":0,"dire_team_id":0,"players":[{"account_id":4294967295,"player_slot":0,"hero_id":62},{"account_id":198771279,"player_slot":1,"hero_id":63},{"account_id":136647688,"player_slot":2,"hero_id":51},{"account_id":136824439,"player_slot":3,"hero_id":4},{"account_id":136229543,"player_slot":4,"hero_id":47},{"account_id":110055836,"player_slot":128,"hero_id":106},{"account_id":136421440,"player_slot":129,"hero_id":61},{"account_id":102905806,"player_slot":130,"hero_id":23},{"account_id":137381548,"player_slot":131,"hero_id":21},{"account_id":352969815,"player_slot":132,"hero_id":18}]},{"match_id":3415777023,"match_seq_num":2977508400,"start_time":1504194890,"lobby_type":0,"radiant_team_id":0,"dire_team_id":0,"players":[{"account_id":413993596,"player_slot":0,"hero_id":76},{"account_id":138833513,"player_slot":1,"hero_id":4},{"account_id":136647688,"player_slot":2,"hero_id":41},{"account_id":4294967295,"player_slot":3,"hero_id":62},{"account_id":4294967295,"player_slot":4,"hero_id":68},{"account_id":187948836,"player_slot":128,"hero_id":47},{"account_id":118446289,"player_slot":129,"hero_id":5},{"account_id":110073913,"player_slot":130,"hero_id":8},{"account_id":131276383,"player_slot":131,"hero_id":23},{"account_id":4294967295,"player_slot":132,"hero_id":35}]},{"match_id":3415671204,"match_seq_num":2977420431,"start_time":1504191955,"lobby_type":0,"radiant_team_id":0,"dire_team_id":0,"players":[{"account_id":169049722,"player_slot":0,"hero_id":21},{"account_id":4294967295,"player_slot":1,"hero_id":109},{"account_id":138833513,"player_slot":2,"hero_id":60},{"account_id":136647688,"player_slot":3,"hero_id":12},{"account_id":133684748,"player_slot":4,"hero_id":6},{"account_id":117186033,"player_slot":128,"hero_id":38},{"account_id":242538156,"player_slot":129,"hero_id":15},{"account_id":4294967295,"player_slot":130,"hero_id":90},{"account_id":197888610,"player_slot":131,"hero_id":88},{"account_id":4294967295,"player_slot":132,"hero_id":114}]},{"match_id":3415567511,"match_seq_num":2977327365,"start_time":1504189218,"lobby_type":0,"radiant_team_id":0,"dire_team_id":0,"players":[{"account_id":4294967295,"player_slot":0,"hero_id":75},{"account_id":334781940,"player_slot":1,"hero_id":4},{"account_id":231010913,"player_slot":2,"hero_id":44},{"account_id":157014284,"player_slot":3,"hero_id":41},{"account_id":408746853,"player_slot":4,"hero_id":102},{"account_id":4294967295,"player_slot":128,"hero_id":69},{"account_id":182535040,"player_slot":129,"hero_id":45},{"account_id":4294967295,"player_slot":130,"hero_id":63},{"account_id":136647688,"player_slot":131,"hero_id":27},{"account_id":138833513,"player_slot":132,"hero_id":71}]},{"match_id":3413689769,"match_seq_num":2975786506,"start_time":1504111539,"lobby_type":0,"radiant_team_id":0,"dire_team_id":0,"players":[{"account_id":4294967295,"player_slot":0,"hero_id":53},{"account_id":4294967295,"player_slot":1,"hero_id":48},{"account_id":343543276,"player_slot":2,"hero_id":7},{"account_id":303304761,"player_slot":3,"hero_id":87},{"account_id":163555668,"player_slot":4,"hero_id":36},{"account_id":178700302,"player_slot":128,"hero_id":114},{"account_id":138833513,"player_slot":129,"hero_id":11},{"account_id":136647688,"player_slot":130,"hero_id":16},{"account_id":388533152,"player_slot":131,"hero_id":93},{"account_id":303144470,"player_slot":132,"hero_id":21}]}]}
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * status : 1
         * num_results : 5
         * total_results : 500
         * results_remaining : 495
         * matches : [{"match_id":3420117698,"match_seq_num":2981004766,"start_time":1504365180,"lobby_type":0,"radiant_team_id":0,"dire_team_id":0,"players":[{"account_id":4294967295,"player_slot":0,"hero_id":62},{"account_id":198771279,"player_slot":1,"hero_id":63},{"account_id":136647688,"player_slot":2,"hero_id":51},{"account_id":136824439,"player_slot":3,"hero_id":4},{"account_id":136229543,"player_slot":4,"hero_id":47},{"account_id":110055836,"player_slot":128,"hero_id":106},{"account_id":136421440,"player_slot":129,"hero_id":61},{"account_id":102905806,"player_slot":130,"hero_id":23},{"account_id":137381548,"player_slot":131,"hero_id":21},{"account_id":352969815,"player_slot":132,"hero_id":18}]},{"match_id":3415777023,"match_seq_num":2977508400,"start_time":1504194890,"lobby_type":0,"radiant_team_id":0,"dire_team_id":0,"players":[{"account_id":413993596,"player_slot":0,"hero_id":76},{"account_id":138833513,"player_slot":1,"hero_id":4},{"account_id":136647688,"player_slot":2,"hero_id":41},{"account_id":4294967295,"player_slot":3,"hero_id":62},{"account_id":4294967295,"player_slot":4,"hero_id":68},{"account_id":187948836,"player_slot":128,"hero_id":47},{"account_id":118446289,"player_slot":129,"hero_id":5},{"account_id":110073913,"player_slot":130,"hero_id":8},{"account_id":131276383,"player_slot":131,"hero_id":23},{"account_id":4294967295,"player_slot":132,"hero_id":35}]},{"match_id":3415671204,"match_seq_num":2977420431,"start_time":1504191955,"lobby_type":0,"radiant_team_id":0,"dire_team_id":0,"players":[{"account_id":169049722,"player_slot":0,"hero_id":21},{"account_id":4294967295,"player_slot":1,"hero_id":109},{"account_id":138833513,"player_slot":2,"hero_id":60},{"account_id":136647688,"player_slot":3,"hero_id":12},{"account_id":133684748,"player_slot":4,"hero_id":6},{"account_id":117186033,"player_slot":128,"hero_id":38},{"account_id":242538156,"player_slot":129,"hero_id":15},{"account_id":4294967295,"player_slot":130,"hero_id":90},{"account_id":197888610,"player_slot":131,"hero_id":88},{"account_id":4294967295,"player_slot":132,"hero_id":114}]},{"match_id":3415567511,"match_seq_num":2977327365,"start_time":1504189218,"lobby_type":0,"radiant_team_id":0,"dire_team_id":0,"players":[{"account_id":4294967295,"player_slot":0,"hero_id":75},{"account_id":334781940,"player_slot":1,"hero_id":4},{"account_id":231010913,"player_slot":2,"hero_id":44},{"account_id":157014284,"player_slot":3,"hero_id":41},{"account_id":408746853,"player_slot":4,"hero_id":102},{"account_id":4294967295,"player_slot":128,"hero_id":69},{"account_id":182535040,"player_slot":129,"hero_id":45},{"account_id":4294967295,"player_slot":130,"hero_id":63},{"account_id":136647688,"player_slot":131,"hero_id":27},{"account_id":138833513,"player_slot":132,"hero_id":71}]},{"match_id":3413689769,"match_seq_num":2975786506,"start_time":1504111539,"lobby_type":0,"radiant_team_id":0,"dire_team_id":0,"players":[{"account_id":4294967295,"player_slot":0,"hero_id":53},{"account_id":4294967295,"player_slot":1,"hero_id":48},{"account_id":343543276,"player_slot":2,"hero_id":7},{"account_id":303304761,"player_slot":3,"hero_id":87},{"account_id":163555668,"player_slot":4,"hero_id":36},{"account_id":178700302,"player_slot":128,"hero_id":114},{"account_id":138833513,"player_slot":129,"hero_id":11},{"account_id":136647688,"player_slot":130,"hero_id":16},{"account_id":388533152,"player_slot":131,"hero_id":93},{"account_id":303144470,"player_slot":132,"hero_id":21}]}]
         */

        private int status;
        private int num_results;
        private int total_results;
        private int results_remaining;
        private List<MatchesBean> matches;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getNum_results() {
            return num_results;
        }

        public void setNum_results(int num_results) {
            this.num_results = num_results;
        }

        public int getTotal_results() {
            return total_results;
        }

        public void setTotal_results(int total_results) {
            this.total_results = total_results;
        }

        public int getResults_remaining() {
            return results_remaining;
        }

        public void setResults_remaining(int results_remaining) {
            this.results_remaining = results_remaining;
        }

        public List<MatchesBean> getMatches() {
            return matches;
        }

        public void setMatches(List<MatchesBean> matches) {
            this.matches = matches;
        }

        public static class MatchesBean {
            /**
             * match_id : 3420117698
             * match_seq_num : 2981004766
             * start_time : 1504365180
             * lobby_type : 0
             * radiant_team_id : 0
             * dire_team_id : 0
             * players : [{"account_id":4294967295,"player_slot":0,"hero_id":62},{"account_id":198771279,"player_slot":1,"hero_id":63},{"account_id":136647688,"player_slot":2,"hero_id":51},{"account_id":136824439,"player_slot":3,"hero_id":4},{"account_id":136229543,"player_slot":4,"hero_id":47},{"account_id":110055836,"player_slot":128,"hero_id":106},{"account_id":136421440,"player_slot":129,"hero_id":61},{"account_id":102905806,"player_slot":130,"hero_id":23},{"account_id":137381548,"player_slot":131,"hero_id":21},{"account_id":352969815,"player_slot":132,"hero_id":18}]
             */

            private long match_id;
            private long match_seq_num;
            private int start_time;
            private int lobby_type;
            private int radiant_team_id;
            private int dire_team_id;
            private List<PlayersBean> players;

            public long getMatch_id() {
                return match_id;
            }

            public void setMatch_id(long match_id) {
                this.match_id = match_id;
            }

            public long getMatch_seq_num() {
                return match_seq_num;
            }

            public void setMatch_seq_num(long match_seq_num) {
                this.match_seq_num = match_seq_num;
            }

            public int getStart_time() {
                return start_time;
            }

            public void setStart_time(int start_time) {
                this.start_time = start_time;
            }

            public int getLobby_type() {
                return lobby_type;
            }

            public void setLobby_type(int lobby_type) {
                this.lobby_type = lobby_type;
            }

            public int getRadiant_team_id() {
                return radiant_team_id;
            }

            public void setRadiant_team_id(int radiant_team_id) {
                this.radiant_team_id = radiant_team_id;
            }

            public int getDire_team_id() {
                return dire_team_id;
            }

            public void setDire_team_id(int dire_team_id) {
                this.dire_team_id = dire_team_id;
            }

            public List<PlayersBean> getPlayers() {
                return players;
            }

            public void setPlayers(List<PlayersBean> players) {
                this.players = players;
            }

            public static class PlayersBean {
                /**
                 * account_id : 4294967295
                 * player_slot : 0
                 * hero_id : 62
                 */

                private long account_id;
                private int player_slot;
                private int hero_id;

                public long getAccount_id() {
                    return account_id;
                }

                public void setAccount_id(long account_id) {
                    this.account_id = account_id;
                }

                public int getPlayer_slot() {
                    return player_slot;
                }

                public void setPlayer_slot(int player_slot) {
                    this.player_slot = player_slot;
                }

                public int getHero_id() {
                    return hero_id;
                }

                public void setHero_id(int hero_id) {
                    this.hero_id = hero_id;
                }
            }
        }
    }
}
