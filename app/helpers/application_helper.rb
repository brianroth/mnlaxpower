module ApplicationHelper
  def opponent(team, game)
    if game.home_team.id == team.id
      game.away_team
    else
      game.home_team
    end
  end

  def goals_for(team, game)
    if game.home_team.id == team.id
      game.home_team_score
    else
      game.away_team_score
    end
  end

  def goals_against(team, game)
    if game.home_team.id == team.id
      game.away_team_score
    else
      game.home_team_score
    end
  end
end
